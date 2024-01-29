package com.sap.cx.boosters.easy.gradleplugin.tasks

import com.sap.cx.boosters.easy.gradleplugin.plugin.EasyPlugin
import com.sap.cx.boosters.easy.gradleplugin.util.CommerceExtensionUtil
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

class DumpPlatformClassPathTask extends DefaultTask {

    public static final String PROP_SAP_COMMERCE_PLATFORM_START_PARAMETER = 'commercePlatformHome'

    @Input
    @Optional
    String commercePlatformHome

    void init() {
        this.initializeCommercePlatformHome()
        group = 'easy'
        description = 'dumps the platform classpath to easy extensions'
    }

    void initializeCommercePlatformHome() {
        if (null == this.commercePlatformHome || this.commercePlatformHome.isBlank()) {
            def configuredCommercePlatformHome = project.gradle.startParameter.projectProperties.commercePlatformHome
            if (null != configuredCommercePlatformHome && !configuredCommercePlatformHome.isBlank()) {
                this.commercePlatformHome = configuredCommercePlatformHome
            } else if (project.properties.containsKey(EasyPlugin.PROP_COMMERCE_PLATFORM_HOME)) {
                this.commercePlatformHome = project.properties.get(EasyPlugin.PROP_COMMERCE_PLATFORM_HOME)
                if (null == this.commercePlatformHome || this.commercePlatformHome.isBlank()) {
                    throw new GradleException('Commerce platform home is not configured.')
                }
            }
        }
    }

    @TaskAction
    def dumpClassPath() {

        init()

        if (null == this.commercePlatformHome || this.commercePlatformHome.isBlank()) {
            project.logger.warn "no commerce platform home is set, specify commercePlatformHome in gradle.properties file"
        } else {
            println "commercePlatformHome: ${commercePlatformHome}"
            def classPath = CommerceExtensionUtil.buildPlatformClassPath(this.commercePlatformHome)
            classPath.each {k,v ->
                println k
                v.each {println "  ${it.canonicalPath}"}
            }
        }

    }

}
