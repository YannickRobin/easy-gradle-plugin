package com.sap.cx.boosters.easy.plugin.tasks

import org.gradle.api.tasks.TaskAction

abstract class InstallEasyExtensionTask : AbstractEasyExtensionTask() {

    init {
        description = "installs an easy extension"
    }

    @TaskAction
    fun installExtension() {
        submitEvent("${restClient.uri}/repository/$repositoryCode/extension/$extensionId/install");
    }

}
