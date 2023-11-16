package com.sap.cx.boosters.easy.gradleplugin.util

import com.sap.cx.boosters.easy.gradleplugin.data.CommerceExtensionInfo
import groovy.io.FileType
import groovy.text.GStringTemplateEngine
import org.slf4j.Logger

import java.nio.file.Path

import org.slf4j.LoggerFactory

class CommerceExtensionUtil {

    private static Logger LOG = LoggerFactory.getLogger(CommerceExtensionUtil)

    static Set<File> buildPlatformClassPath(String commercePlatformHome) {

        def classPath = [] as Set<File>

        if (!commercePlatformHome) {
            return classPath
        }

        def commerceHomeDirectory = new File(resolveHome(commercePlatformHome))
        def commercePlatformDirectory = new File(commerceHomeDirectory,'bin/platform')

        if (!commercePlatformDirectory.exists()) {
            LOG.error "commerce platform dir not found ${commercePlatformDirectory.absolutePath}"
            return classPath
        }

        def localExtensionsFile = new File(commerceHomeDirectory,'config/localextensions.xml')
        if (!localExtensionsFile.exists()) {
            LOG.error "no localextensions.xml file found ${localExtensionsFile.absolutePath}"
            return classPath
        }

        commercePlatformDirectory.traverse(type: FileType.FILES, nameFilter: ~/.*\.jar$/) {
            classPath << it
        }
        commercePlatformDirectory.traverse(type: FileType.DIRECTORIES, nameFilter: 'classes') {
            if (!it.absolutePath.contains('eclipsebin')) classPath << it
        }

        def extensions = getExtensions(localExtensionsFile)

        def jarFilter = {dir,name -> name.endsWith('.jar')} as FilenameFilter

        extensions.each{info ->

            LOG.debug "adding classpath for extension: ${info.name}"
            def extBinDir = new File(info.rootPath,'bin')
            if (extBinDir.exists()) classPath.addAll(extBinDir.listFiles(jarFilter))

            def extLibDir = new File(info.rootPath,'lib')
            if (extLibDir.exists()) classPath.addAll(extLibDir.listFiles(jarFilter))

            def extClassesDir = new File(info.rootPath,'classes')
            if (extClassesDir.exists()) classPath.add(extClassesDir)

            def resourcesDir = new File(info.rootPath,'resources')
            if (resourcesDir.exists()) classPath.add(resourcesDir)

        }

        classPath.each{it -> LOG.debug it.canonicalPath}

        classPath

    }

    static Set<CommerceExtensionInfo> getExtensions(File localExtensionsFile) {

        def xmlParser = new groovy.xml.XmlSlurper()
        def templateEngine = new GStringTemplateEngine()

        def hybrisConfig = xmlParser.parse(localExtensionsFile)
        def hybrisBinDir = Path.of(localExtensionsFile.parentFile.parent,'bin').toFile()
        def bindMap = [HYBRIS_BIN_DIR:hybrisBinDir.absolutePath]

        def scanPath = {String path ->

            def extensions = [] as Set<CommerceExtensionInfo>
            def extensionPath = new File(templateEngine.createTemplate(path).make(bindMap).toString())
            extensionPath.traverse(type: FileType.FILES, nameFilter: 'extensioninfo.xml', maxDepth: 3) {

                LOG.debug "parsing extensioninfo file: ${it}"

                def extensioninfo = xmlParser.parse(it)
                def extensionName = extensioninfo.extension[0].'@name'.text()
                def requiresExtension = extensioninfo.extension[0].'requires-extension'.collect{it.'@name'.text()}
                def coremodule = extensioninfo.extension[0].'coremodule'.size() > 0
                def webmodule = extensioninfo.extension[0].'webmodule'.size() > 0
                def info = new CommerceExtensionInfo(
                        name: extensionName,
                        rootPath: it.parentFile,
                        coremodule: coremodule,
                        webmodule: webmodule,
                        requires: requiresExtension
                )
                extensions << info

            }

            return extensions

        }

        def allExtensions = [] as Set<CommerceExtensionInfo>
        def paths = hybrisConfig.extensions[0].path.collect{it.'@dir'.text()} as List<String>

        paths.each{path ->
            LOG.debug "searching extensions in path: ${path}"
            allExtensions.addAll(scanPath(path))
        }

        def coreExtensions = scanPath('$HYBRIS_BIN_DIR/platform/ext')
        def allExtensionsMap = allExtensions.collectEntries{[(it.name):it]} as Map<String,CommerceExtensionInfo>
        coreExtensions.each {allExtensionsMap.put(it.name,it)}

        def configuredExtensionNames = coreExtensions*.name
        configuredExtensionNames += hybrisConfig.extensions[0].extension.collect{it.'@name'.text()}
        def requiredExtensions = [] as Set<CommerceExtensionInfo>

        def add
        add = {String extName ->
            LOG.debug "adding configured extension: ${extName}"
            def extInfo = allExtensionsMap[extName]
            if (extInfo) {
                if (requiredExtensions.add(extInfo)) {
                    if (allExtensionsMap[extName] && allExtensionsMap[extName].requires) {
                        allExtensionsMap[extName].requires.each{require ->
                            LOG.debug "adding required extension: ${require}"
                            // NOTE trampoline doesn't to work here
                            // add.trampoline(require)
                            add(require)
                        }
                    }
                }
            } else {
                LOG.warn "skipped invalid extension ${extName}"
            }
        }

        configuredExtensionNames.each{add(it)}

        return requiredExtensions

    }

    static String resolveHome(String path) {
        path.replace('~',System.getProperty('user.home'))
    }

}
