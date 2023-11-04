package com.sap.cx.boosters.easy.gradleplugin

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

        def localExtensions = new File(commercePlatformHome,'config/localextensions.xml')
        if (!localExtensions.exists()) {
            println "no localextensions.xml file found ${localExtensions.absolutePath}"
            return classPath
        }

        commercePlatformHomeDirectory.traverse(type: groovy.io.FileType.FILES, nameFilter: ~/.*\.jar$/) {
            classPath << it
        }
        commercePlatformHomeDirectory.traverse(type: groovy.io.FileType.DIRECTORIES, nameFilter: 'classes') {
            if (!it.absolutePath.contains('eclipsebin')) classPath << it
        }

        def xmlParser = new groovy.xml.XmlSlurper()
        def hybrisConfig = xmlParser.parse(localExtensions)
        def paths = hybrisConfig.extensions[0].path.collect{it.'@dir'.text()} as List<String>
        def bindMap = [HYBRIS_BIN_DIR:"${commercePlatformHome}/bin"]
        def templateEngine = new groovy.text.GStringTemplateEngine()
        def extensionInfoMap = [:] as Map<String,Map<String,?>>

        def corePath = '$HYBRIS_BIN_DIR/platform/ext'
        paths << corePath

        def allExtensionNames = [] as Set

        paths.each{p ->

            println "path: ${p}"

            def extensionPath = new File(templateEngine.createTemplate(p).make(bindMap).toString())
            extensionPath.traverse(type: groovy.io.FileType.FILES, nameFilter: 'extensioninfo.xml', maxDepth: 3) {

                println "file: ${it}"

                def extensioninfo = xmlParser.parse(it)
                def extensionName = extensioninfo.extension[0].'@name'.text()
                def requiresExtension = extensioninfo.extension[0].'requires-extension'.collect{it.'@name'.text()}
                def coremodule = extensioninfo.extension[0].'coremodule'.size() > 0
                def webmodule = extensioninfo.extension[0].'webmodule'.size() > 0
                def info = [
                        name:extensionName,
                        root:it.parentFile,
                        coremodule:coremodule,
                        webmodule:webmodule,
                        requires:requiresExtension
                ]
                println "Adding extension: ${extensionName} to map"
                extensionInfoMap.put(extensionName,info)
                // println "${it} ${info}"
                if (p == corePath) allExtensionNames << extensionName

            }

        }

        def extNames = hybrisConfig.extensions[0].extension.collect{it.'@name'.text()}

        def add
        add = {String extName ->
            println "Adding extension: ${extName}"
            if (!allExtensionNames.add(extName)) {
                if (extensionInfoMap[extName] && extensionInfoMap[extName].requires) {
                    extensionInfoMap[extName].requires.each{require ->
                        println "Adding required extension: ${require}"
                        add.trampoline(require)}
                }
            }
        }

        extNames.each{add(it)}

        def jarFilter = {dir,name -> name.endsWith('.jar')} as FilenameFilter

        allExtensionNames.each{

            println "extension: ${it}"

            def info = extensionInfoMap[it]

            if (info) {
                println "Configuring extension: ${it}"
                def extBinDir = new File(info.root,'bin')
                if (extBinDir.exists()) classPath.addAll(extBinDir.listFiles(jarFilter)*.canonicalPath)

                def extLibDir = new File(info.root,'lib')
                if (extLibDir.exists()) classPath.addAll(extLibDir.listFiles(jarFilter)*.canonicalPath)

                def extClassesDir = new File(info.root,'classes')
                if (extClassesDir.exists()) classPath.add(extClassesDir.canonicalPath)

                def resourcesDir = new File(info.root,'resources')
                if (resourcesDir.exists()) classPath.add(resourcesDir.canonicalPath)

            }
        }

        classPath.each{it -> println it}

        classPath

    }

    static String resolveHome(String path) {
        path.replace('~',System.getProperty('user.home'))
    }

}