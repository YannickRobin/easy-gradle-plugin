package com.sap.cx.boosters.easy.plugin.tasks

import org.gradle.api.tasks.TaskAction

abstract class UnInstallEasyExtensionTask : AbstractEasyExtensionTask() {

    init {
        description = "uninstalls an easy extension"
    }

    @TaskAction
    fun reinstallExtension() {
        submitEvent("${restClient.uri}/repository/$repositoryCode/extension/$extensionId/uninstall");
    }

}
