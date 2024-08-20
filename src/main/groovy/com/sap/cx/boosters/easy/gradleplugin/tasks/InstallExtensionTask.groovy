package com.sap.cx.boosters.easy.gradleplugin.tasks


import com.fasterxml.jackson.databind.ObjectMapper
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.HttpResponseException
import org.apache.commons.collections.MapUtils
import org.apache.commons.lang.StringUtils
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

import static groovyx.net.http.ContentType.JSON

class InstallExtensionTask extends AbstractEasyExtensionTask {

    @Input
    @Optional
    String configuration

    @Input
    @Optional
    String configurationPath

    @Option(option = 'configuration', description = 'Install configuration for the extension installation')
    void setConfiguration(String configuration) {
        this.configuration = configuration
    }

    @Option(option = 'configurationPath', description = 'Install configuration file for the extension installation')
    void setConfigurationFile(String configurationPath) {
        this.configurationPath = configurationPath
    }

    @Override
    void init() {
        super.init()
        description = 'installs an easy extension'
    }

    @TaskAction
    def install() {
        init()

        def configurationSchemaFile = new File(project.projectDir, '/src/main/resources/easy-installer-wizard.json')
        def configurationFile
        if (StringUtils.isEmpty(this.configurationPath)) {
            configurationFile = new File(project.projectDir, '/src/main/resources/install-config.json')
        } else {
            configurationFile = new File(this.configurationPath)
        }

        if (configurationSchemaFile.exists()) {
            def mapper = new ObjectMapper()
            if (this.configuration == null) {
                if (configurationFile.exists()) {
                    println "Using install configurations configured from file " +  configurationFile.getAbsolutePath()
                    this.configuration = configurationFile.text
                } else {
                    // Read from Easy Extension and use if available
                    println "Retrieving install configurations from remote"
                    def response = restClient.get(path: "$easyApiBaseUrl/repository/$repositoryCode/extension/$extensionId") as HttpResponseDecorator
                    if (response.status == 200 && null != response.data && MapUtils.isNotEmpty(response.data.installConfiguration)) {
                        logger.info "Using install configurations retrieved from remote"
                        this.configuration = mapper.writeValueAsString(response.data.installConfiguration)
                    } else {
                        logger.error("Install configurations are not available at remote.")
                    }
                }
            } else {
                logger.info "Using install configurations configured in 'command-line argument'"
            }

        }
        this.processInstallation()

    }

    private void processInstallation() {
        try {
            def response = restClient.post(
                    path: "$easyApiBaseUrl/repository/$repositoryCode/extension/$extensionId/install",
                    body: this.configuration ? this.configuration : null,
                    requestContentType: JSON,
                    query: [async: false]

            )

            printResponse(response)
        } catch (HttpResponseException exception) {
            if (exception.response) {
                throw new GradleException(String.format("[Error] - %s", exception.response.data.errors[0].message))
            } else {
                throw new GradleException("Installation failed. Refer to logs for error details.")
            }
        }
    }

}
