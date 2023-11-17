package com.sap.cx.boosters.easy.gradleplugin.tasks

import com.sap.cx.boosters.easy.gradleplugin.util.CommerceExtensionUtil
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

class DumpPlatformClassPathTask extends DefaultTask {

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
            String configuredCommercePlatformHome = project.gradle.startParameter.projectProperties.commercePlatformHome
            if (null != configuredCommercePlatformHome && !configuredCommercePlatformHome.isBlank()) {
                this.commercePlatformHome = configuredCommercePlatformHome
            } else if (project.properties.containsKey('sap.commerce.platform.home')) {
                this.commercePlatformHome = project.properties.get('sap.commerce.platform.home')
                if (null == this.commercePlatformHome || this.commercePlatformHome.isBlank()) {
                    throw new GradleException('Commerce platform home is not configured.')
                }
            }
        }
    }

    @TaskAction
    def dumpClassPath() {
        def classPath = CommerceExtensionUtil.buildPlatformClassPath(this.commercePlatformHome)
        classPath.each { println it.canonicalPath }
    }

}
