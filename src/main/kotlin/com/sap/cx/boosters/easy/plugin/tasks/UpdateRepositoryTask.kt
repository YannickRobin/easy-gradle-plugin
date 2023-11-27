package com.sap.cx.boosters.easy.plugin.tasks

import org.gradle.api.tasks.TaskAction

abstract class UpdateRepositoryTask : AbstractEasyTask() {

    init {
        description = "updates an easy repository from the remote location"
    }

    @TaskAction
    fun updateRepository() {
        submitEvent("${restClient.uri}/repository/$repositoryCode/update")
    }
}
