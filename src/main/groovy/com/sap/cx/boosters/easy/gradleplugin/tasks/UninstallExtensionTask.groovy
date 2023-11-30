package com.sap.cx.boosters.easy.gradleplugin.tasks

import org.gradle.api.tasks.TaskAction

class UninstallExtensionTask extends AbstractEasyExtensionTask {

    @Override
    void init() {
        super.init()
        description = 'uninstalls an easy extension'
    }

    @TaskAction
    def uninstall() {
        init()
        restClient.post(
                path: "$easyApiBaseUrl/repository/$repositoryCode/extension/$extensionId/uninstall",
                query: ['async': 'false']
        )
    }

}
