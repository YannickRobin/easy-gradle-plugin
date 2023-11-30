package com.sap.cx.boosters.easy.gradleplugin.tasks

import org.codehaus.groovy.GroovyException
import org.gradle.api.tasks.TaskAction

class UninstallExtensionTask extends AbstractEasyExtensionTask {

    @Override
    void init() {
        super.init()
        description = 'uninstalls an easy extension'
    }

    @TaskAction
    def uninstall() {
        if (project == project.gradle.rootProject) {
            throw new GroovyException('Extension uninstallation cannot execute from the root project')
        } else {
            init()
            restClient.post(
                    path: "$easyApiBaseUrl/repository/$repositoryCode/extension/$extensionId/uninstall",
                    query: ['async': 'false']
            )
        }
    }

}
