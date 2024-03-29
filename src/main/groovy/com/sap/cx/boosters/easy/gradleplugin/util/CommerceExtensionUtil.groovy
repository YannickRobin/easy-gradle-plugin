package com.sap.cx.boosters.easy.gradleplugin.util

import com.sap.cx.boosters.easy.gradleplugin.data.CommerceExtensionInfo
import groovy.io.FileType
import groovy.text.GStringTemplateEngine
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.nio.file.Path

class CommerceExtensionUtil {

    static Logger log = LoggerFactory.getLogger(CommerceExtensionUtil)

    static Set<File> buildPlatformClassPath(String commercePlatformHome) {

        def classPath = [] as Set<File>

        if (!commercePlatformHome) {
            return classPath
        }

        def commerceHomeDirectory = new File(resolveHome(commercePlatformHome))
        def commercePlatformDirectory = new File(commerceHomeDirectory, 'bin/platform')

        if (!commercePlatformDirectory.exists()) {
            println "commerce platform dir not found ${commercePlatformDirectory.absolutePath}"
            return classPath
        }

        def localExtensionsFile = new File(commerceHomeDirectory, 'config/localextensions.xml')
        if (!localExtensionsFile.exists()) {
            println "no localextensions.xml file found ${localExtensionsFile.absolutePath}"
            return classPath
        }

        commercePlatformDirectory.traverse(type: FileType.FILES, nameFilter: ~/.*\.jar$/) {
            classPath << it
        }
        commercePlatformDirectory.traverse(type: FileType.DIRECTORIES, nameFilter: 'classes') {
            if (!it.absolutePath.contains('eclipsebin')) classPath << it
        }

        def extensions = getExtensions(localExtensionsFile)

        def jarFilter = { dir, name -> name.endsWith('.jar') } as FilenameFilter

        extensions.each { info ->

            log.debug "adding classpath for extension: ${info.name}"
            def extBinDir = new File(info.rootPath, 'bin')
            if (extBinDir.exists()) classPath.addAll(extBinDir.listFiles(jarFilter))

            def extLibDir = new File(info.rootPath, 'lib')
            if (extLibDir.exists()) classPath.addAll(extLibDir.listFiles(jarFilter))

            def extClassesDir = new File(info.rootPath, 'classes')
            if (extClassesDir.exists()) classPath.add(extClassesDir)

            def resourcesDir = new File(info.rootPath, 'resources')
            if (resourcesDir.exists()) classPath.add(resourcesDir)

        }

        classPath.each { it -> log.debug it.canonicalPath }

        classPath

    }

    static Set<CommerceExtensionInfo> getExtensions(File localExtensionsFile) {

        def xmlParser = new groovy.xml.XmlSlurper()
        def templateEngine = new GStringTemplateEngine()

        def hybrisConfig = xmlParser.parse(localExtensionsFile)
        def hybrisBinDir = Path.of(localExtensionsFile.parentFile.parent, 'bin').toFile()
        def bindMap = [HYBRIS_BIN_DIR: hybrisBinDir.absolutePath]
        def resolvePath = { String path -> templateEngine.createTemplate(path).make(bindMap).toString() }

        def parseExtensionInfo = { File it ->

            def extensioninfo = xmlParser.parse(it)
            def extensionName = extensioninfo.extension[0].'@name'.text()
            def requiresExtension = extensioninfo.extension[0].'requires-extension'.collect { it.'@name'.text() }
            def coremodule = extensioninfo.extension[0].'coremodule'.size() > 0
            def webmodule = extensioninfo.extension[0].'webmodule'.size() > 0
            def info = new CommerceExtensionInfo(
                    name: extensionName,
                    rootPath: it.parentFile,
                    coremodule: coremodule,
                    webmodule: webmodule,
                    requires: requiresExtension
            )
            info

        }

        def scanPath = { String path ->

            def extensions = [] as Set<CommerceExtensionInfo>
            def extensionPath = new File(resolvePath(path))
            extensionPath.traverse(type: FileType.FILES, nameFilter: 'extensioninfo.xml', maxDepth: 3) {
                log.debug "parsing extensioninfo file: ${it}"
                extensions << parseExtensionInfo(it)
            }

            return extensions

        }

        def allExtensions = [] as Set<CommerceExtensionInfo>
        def paths = hybrisConfig.extensions[0].path.collect { it.'@dir'.text() } as List<String>

        paths.each { path ->
            if (path == '\${HYBRIS_BIN_DIR}') {
                path = bindMap.HYBRIS_BIN_DIR
            }
            println("searching extensions in path: $path")
            allExtensions.addAll(scanPath(path))
        }

        def coreExtensions = scanPath('$HYBRIS_BIN_DIR/platform/ext')
        def allExtensionsMap = allExtensions.collectEntries { [(it.name): it] } as Map<String, CommerceExtensionInfo>

        coreExtensions.each { allExtensionsMap.put(it.name, it) }
        def configuredExtensionNames = coreExtensions*.name

        configuredExtensionNames += hybrisConfig.extensions[0].extension.collect { it.'@name'.text() }.findAll { it }

        // adding extensions configured with absolute path dir
        hybrisConfig.extensions[0].extension.collect { it.'@dir'.text() }.findAll { it }.each { extBaseDir ->
            def info = parseExtensionInfo(new File(resolvePath(extBaseDir), 'extensioninfo.xml'))
            allExtensionsMap[info.name] = info
            configuredExtensionNames << info.name
        }

        def requiredExtensions = [] as Set<CommerceExtensionInfo>

        def add
        add = { String extName ->
            log.debug "adding configured extension: ${extName}"
            def extInfo = allExtensionsMap[extName]
            if (extInfo) {
                if (requiredExtensions.add(extInfo)) {
                    if (allExtensionsMap[extName] && allExtensionsMap[extName].requires) {
                        allExtensionsMap[extName].requires.each { require ->
                            log.debug "adding required extension: ${require}"
                            // NOTE trampoline doesn't to work here
                            // add.trampoline(require)
                            add(require)
                        }
                    }
                }
            } else {
                log.debug "skipped invalid extension ${extName}"
            }
        }

        configuredExtensionNames.each { add(it) }

        return requiredExtensions

    }

    static String resolveHome(String path) {
        path.replace('~', System.getProperty('user.home'))
    }

}
