package com.sap.cx.boosters.easy.gradleplugin.tasks

import org.codehaus.groovy.GroovyException
import org.gradle.api.tasks.TaskAction

class ReInstallExtensionTask extends AbstractEasyExtensionTask {

    @Override
    void init() {
        super.init()
        description = 're-installs/reloads an easy extension'
    }

    @TaskAction
    def reinstall() {
        if (project == project.gradle.rootProject) {
            throw new GroovyException('Extension re-installation cannot execute from the root project')
        }else {
            init()
            restClient.post(
                    path: "$easyApiBaseUrl/repository/$repositoryCode/extension/$extensionId/reinstall",
                    query: ['async': 'false']
            )
        }
    }

}
