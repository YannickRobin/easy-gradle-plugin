package com.sap.cx.boosters.easy.plugin.tasks

import org.gradle.api.tasks.TaskAction

abstract class ReInstallEasyExtensionTask : AbstractEasyExtensionTask() {

    init {
        description = "re-installs/reloads an easy extension"
    }

    @TaskAction
    fun reinstallExtension() {
        submitEvent("${restClient.uri}/repository/$repositoryCode/extension/$extensionId/reinstall");
    }

}
