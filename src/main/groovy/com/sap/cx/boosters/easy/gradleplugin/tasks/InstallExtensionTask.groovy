package com.sap.cx.boosters.easy.gradleplugin.tasks

import org.gradle.api.tasks.TaskAction

class InstallExtensionTask extends AbstractEasyExtensionTask {

    @Override
    void init() {
        super.init()
        description = 'installs an easy extension'
    }

    @TaskAction
    def install() {
        init()
        restClient.post(
                path: "$easyApiBaseUrl/repository/$repositoryCode/extension/$extensionId/install",
                query: ['async': 'false']
        )
    }

}
