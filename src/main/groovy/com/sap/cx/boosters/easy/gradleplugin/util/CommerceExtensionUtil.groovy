package com.sap.cx.boosters.easy.gradleplugin.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CommerceExtensionUtil {

    static Logger log = LoggerFactory.getLogger(CommerceExtensionUtil)

    public static final String PLATFORM = 'platform'

    static Map<String,Set<File>> buildPlatformClassPath(String commercePlatformHome) {

        def commercePlatformDirectory = new File(resolveHome(commercePlatformHome))
        def bootstrapJar = new File(commercePlatformDirectory,'bootstrap/bin/ybootstrap.jar')

        def classPath = [:] as Map<String,Set<File>>

        if (!bootstrapJar.exists()) {
            println "no bootstrap.jar file found: ${bootstrapJar.absolutePath}"
            return classPath
        }

        def groovyShell = new GroovyShell().tap{
            classLoader.addURL(bootstrapJar.toURI().toURL())
        }

        groovyShell.setVariable('commercePlatformHome',commercePlatformHome)
        def platformConfig = groovyShell.run('de.hybris.bootstrap.config.PlatformConfig.getInstance(de.hybris.bootstrap.config.ConfigUtil.getSystemConfig(commercePlatformHome))','platformConfig.groovy',[] as String [])
        println "platformConfig.platformHome: ${platformConfig.platformHome}"

        def commerceConfigDir = platformConfig.systemConfig.configDir as String

        def localExtensionsFile = new File(commerceConfigDir, 'localextensions.xml')
        if (!localExtensionsFile.exists()) {
            println "no localextensions.xml file found ${localExtensionsFile.absolutePath}"
            return classPath
        }

        groovyShell.setVariable('platformConfig',platformConfig)
        def fullClassPath = groovyShell.run('de.hybris.bootstrap.loader.HybrisClasspathBuilder.getClassPathAsList(platformConfig)','hybrisClassPath.groovy',[] as String []) as Set<File>

        classPath[PLATFORM] = fullClassPath

        // final List<ExtensionInfo> extensions = platformConfig.getExtensionInfosInBuildOrder();
        def extensions = platformConfig.extensionInfosInBuildOrder

        def jarFilter = { dir, name -> name.endsWith('.jar') } as FilenameFilter

        extensions.findAll{it.webExtension}.each{ extInfo ->

            def extensionDirectory = extInfo.extensionDirectory as File
            def extensionName = extInfo.name as String

            log.debug "adding classpath for extension: ${extInfo.name}"
            classPath[extensionName] = [] as Set<File>

            new File(extensionDirectory,'web/webroot/WEB-INF/lib').with{
                if (exists()) classPath[extensionName].addAll(listFiles(jarFilter))
            }

            new File(extensionDirectory,'web/webroot/WEB-INF/classes').with{
                if (exists()) classPath[extensionName].add(it)
            }

        }

        classPath.each { k,v ->
            log.debug k
            v.each { log.debug "  ${it.canonicalPath}"}
        }

        classPath

    }

    static String resolveHome(String path) {
        path.replace('~', System.getProperty('user.home'))
    }

}
