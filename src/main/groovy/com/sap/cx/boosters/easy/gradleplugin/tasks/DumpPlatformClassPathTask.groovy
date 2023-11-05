package com.sap.cx.boosters.easy.gradleplugin.tasks

import com.sap.cx.boosters.easy.gradleplugin.CommerceExtensionUtil
import com.sap.cx.boosters.easy.gradleplugin.EasyPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class DumpPlatformClassPathTask extends DefaultTask {

    @TaskAction
    def dumpClassPath() {
        def classPath = CommerceExtensionUtil.buildPlatformClassPath(this.project.properties[EasyPlugin.PROP_COMMERCE_PLATFORM_HOME])
        classPath.each {println it.canonicalPath}
    }

}
