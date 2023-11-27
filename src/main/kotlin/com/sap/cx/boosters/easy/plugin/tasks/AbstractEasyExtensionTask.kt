package com.sap.cx.boosters.easy.plugin.tasks

import org.apache.commons.lang3.StringUtils
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input

abstract class AbstractEasyExtensionTask : AbstractEasyTask() {
    @Input
    var extensionId: String = ""

    init {
        description = "Installs an easy extension"
        setExtensionId()
    }

    private fun setExtensionId() {
        if (StringUtils.isEmpty(extensionId)) {
            val userInput = project.gradle.startParameter.projectProperties["extensionId"]
            extensionId = if (!userInput.isNullOrBlank()) {
                userInput
            } else if (project.properties.contains("sap.commerce.easy.extension.id")) {
                project.property("sap.commerce.easy.extension.id") as String
            } else {
                logger.info("Enter the easy extension id: ")
                val input = readlnOrNull().toString()
                input.ifBlank {
                    throw GradleException("Easy extension id is required for install-easy-extension task")
                }
            }
        }
    }
}
