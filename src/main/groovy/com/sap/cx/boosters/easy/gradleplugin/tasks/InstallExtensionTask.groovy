package com.sap.cx.boosters.easy.gradleplugin.tasks

import org.codehaus.groovy.GroovyException
import org.gradle.api.tasks.TaskAction

class InstallExtensionTask extends AbstractEasyExtensionTask {

    @Override
    void init() {
        super.init()
        description = 'installs an easy extension'
    }

    @TaskAction
    def install() {
        if (project == project.gradle.rootProject) {
            throw new GroovyException('Extension installation cannot execute from the root project')
        } else {
            init()
            restClient.post(
                    path: "$easyApiBaseUrl/repository/$repositoryCode/extension/$extensionId/install",
                    query: ['async': 'false']
            )
        }
    }

}
