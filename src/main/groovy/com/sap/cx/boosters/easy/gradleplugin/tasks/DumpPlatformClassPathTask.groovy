package com.sap.cx.boosters.easy.gradleplugin.tasks

import com.sap.cx.boosters.easy.gradleplugin.CommerceExtensionHelper
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class DumpPlatformClassPathTask extends DefaultTask {

    @TaskAction
    def dumpClassPath() {
        def extHelper = new CommerceExtensionHelper(this.logger)
        def commercePlatformHome = this.project.properties['commercePlatformHome']
        logger.info "commercePlatformHome: $commercePlatformHome"
        logger.info "commercePlatformHome: ${extHelper.resolveHome(commercePlatformHome)}"
        def classPath = extHelper.buildPlatformClassPath(this.getProject())
        classPath.each {println it.canonicalPath}
    }

}
