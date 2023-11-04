package com.sap.cx.boosters.easy.gradleplugin

import java.nio.file.Path

class EasyPluginUtil {

    static void displayEasyConfigInfo(EasyPluginExtension easyConfig) {
        println "Welcome to Easy Gradle Plugin\n"
        println "SAP Commerce Base URL: ${easyConfig.baseUrl.get()}"
        println "Repository: ${easyConfig.repository.get()}"
        println "Extension: ${easyConfig.extension.get()}\n"
    }

    // FIXME use gradle logging instead of println
    static Set<File> buildPlatformClassPath(String commercePlatformHome) {

        def classPath = [] as Set<File>

        def commercePlatformHomeDirectory = new File(commercePlatformHome,'bin/platform')

        if (!commercePlatformHomeDirectory.exists()) {
            println "platform home not found ${commercePlatformHomeDirectory.absolutePath}"
            return classPath
        }

        def localExtensionsFile = new File(commercePlatformHome,'config/localextensions.xml')
        if (!localExtensionsFile.exists()) {
            println "no localextensions.xml file found ${localExtensionsFile.absolutePath}"
            return classPath
        }

        commercePlatformHomeDirectory.traverse(type: groovy.io.FileType.FILES, nameFilter: ~/.*\.jar$/) {
            classPath << it
        }
        commercePlatformHomeDirectory.traverse(type: groovy.io.FileType.DIRECTORIES, nameFilter: 'classes') {
            if (!it.absolutePath.contains('eclipsebin')) classPath << it
        }

        def configuredExtenions = getExtensions(localExtensionsFile)

        def jarFilter = {dir,name -> name.endsWith('.jar')} as FilenameFilter

        configuredExtenions.each{info ->

            println "extension: ${info.name}"
            def extBinDir = new File(info.rootPath,'bin')
            if (extBinDir.exists()) classPath.addAll(extBinDir.listFiles(jarFilter)*.canonicalPath)

            def extLibDir = new File(info.rootPath,'lib')
            if (extLibDir.exists()) classPath.addAll(extLibDir.listFiles(jarFilter)*.canonicalPath)

            def extClassesDir = new File(info.rootPath,'classes')
            if (extClassesDir.exists()) classPath.add(extClassesDir.canonicalPath)

            def resourcesDir = new File(info.rootPath,'resources')
            if (resourcesDir.exists()) classPath.add(resourcesDir.canonicalPath)

        }

        classPath.each{it -> println it}

        classPath

    }

    static Set<CommerceExtensionInfo> getExtensions(File localExtensionsFile) {

        def xmlParser = new groovy.xml.XmlSlurper()
        def templateEngine = new groovy.text.GStringTemplateEngine()

        def hybrisConfig = xmlParser.parse(localExtensionsFile)
        def hybrisBinDir = Path.of(localExtensionsFile.parentFile.parent,'bin').toFile()
        def bindMap = [HYBRIS_BIN_DIR:hybrisBinDir.absolutePath]

        def scanPath = {String path ->

            def extensions = [] as Set<CommerceExtensionInfo>
            def extensionPath = new File(templateEngine.createTemplate(path).make(bindMap).toString())
            extensionPath.traverse(type: groovy.io.FileType.FILES, nameFilter: 'extensioninfo.xml', maxDepth: 3) {

                println "file: ${it}"

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
                println "Adding extension: ${extensionName} to set"
                extensions << info

            }

            return extensions

        }

        def allExtensions = [] as Set<CommerceExtensionInfo>
        def paths = hybrisConfig.extensions[0].path.collect{it.'@dir'.text()} as List<String>

        paths.each{path ->
            println "path: ${path}"
            allExtensions.addAll(scanPath(path))
        }

        def coreExtensions = scanPath('$HYBRIS_BIN_DIR/platform/ext')
        def allExtensionsMap = allExtensions.collectEntries{[(it.name):it]} as Map<String,CommerceExtensionInfo>
        coreExtensions.each {allExtensionsMap.put(it.name,it)}

        def configuredExtensionNames = coreExtensions*.name
        configuredExtensionNames += hybrisConfig.extensions[0].extension.collect{it.'@name'.text()}
        def requiredExtensionNames = [] as Set<String>

        def add
        add = {String extName ->
            println "Adding extension: ${extName}"
            if (requiredExtensionNames.add(extName)) {
                if (allExtensionsMap[extName] && allExtensionsMap[extName].requires) {
                    allExtensionsMap[extName].requires.each{require ->
                        println "Adding required extension: ${require}"
                        // NOTE trampoline doesn't to work here
                        // add.trampoline(require)
                        add(require)
                    }
                }
            }
        }

        configuredExtensionNames.each{add(it)}

        return requiredExtensionNames.collect{allExtensionsMap[it]}

    }

    static String resolveHome(String path) {
        path.replace('~',System.getProperty('user.home'))
    }

}