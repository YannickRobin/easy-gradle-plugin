package com.sap.cx.boosters.easy.gradleplugin.tasks

import groovy.json.JsonSlurper
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

abstract class AbstractEasyExtensionTask extends AbstractEasyTask {

    @Input
    @Optional
    String extensionId

    void init() {
        super.init()
        this.initializeExtensionId()
    }

    void initializeExtensionId() {
        if (null == this.extensionId || this.extensionId.isBlank()) {
            def configuredExtensionId = project.gradle.startParameter.projectProperties.extensionId
            if (null != configuredExtensionId && !configuredExtensionId.isBlank()) {
                this.extensionId = configuredExtensionId
            } else if (project.properties.containsKey('sap.commerce.easy.extension.id')) {
                this.extensionId = project.properties.get('sap.commerce.easy.extension.id')
            } else if (project.file('easy.json').exists()) {
                this.extensionId = new JsonSlurper().parse(project.file('easy.json')).id
            } else {
                throw new GradleException('Easy extension id is required for easy extension events.')
            }
        }
    }

}
