package com.sap.cx.boosters.easy.gradleplugin.tasks

import com.sap.cx.boosters.easy.gradleplugin.CommerceExtensionUtil
import com.sap.cx.boosters.easy.gradleplugin.EasyPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class DumpPlatformClassPathTask extends DefaultTask {

    @TaskAction
    def dumpClassPath() {
        def property = this.project.properties[EasyPlugin.PROP_COMMERCE_PLATFORM_HOME]
        if (!property) {
            println "property ${EasyPlugin.PROP_COMMERCE_PLATFORM_HOME} is not set"
        } else {
            println "property PROP_COMMERCE_PLATFORM_HOME: ${property}"
        }
        def classPath = CommerceExtensionUtil.buildPlatformClassPath(this.project.properties[EasyPlugin.PROP_COMMERCE_PLATFORM_HOME])
        classPath.each {println it.canonicalPath}
    }

}
