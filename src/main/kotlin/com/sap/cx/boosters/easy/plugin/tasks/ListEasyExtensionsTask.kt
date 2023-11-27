package com.sap.cx.boosters.easy.plugin.tasks

import org.gradle.api.tasks.TaskAction

abstract class ListEasyExtensionsTask : AbstractEasyTask() {

    init {
        description = "lists easy extensions of an easy repository"
    }

    @TaskAction
    fun listExtensions() {
        executeGetRequest("${restClient.uri}/repository/$repositoryCode/extensions")
    }
}
