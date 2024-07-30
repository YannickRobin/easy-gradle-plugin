package com.sap.cx.boosters.easy.gradleplugin.tasks

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.networknt.schema.JsonSchema
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersion
import groovy.json.JsonSlurper
import org.apache.commons.lang3.StringUtils
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

class InstallExtensionTask extends AbstractEasyExtensionTask {

    @Input
    @Optional
    String configuration

    @Option(option = 'configuration', description = 'Wizard configuration for the extension installation')
    void setConfiguration(String configuration) {
        this.configuration = configuration
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
        def configurationFile = new File(project.projectDir, '/src/main/resources/install-config.json')
        if (configurationSchemaFile.exists()) {
            def mapper = new ObjectMapper()
            if (this.configuration == null && configurationFile.exists()) {
                this.configuration = configurationFile.text
            } else {
                // Read from Easy Extension and use if available
                def extension = restClient.get(path: "$easyApiBaseUrl/repository/$repositoryCode/extension/$extensionId")
                if(null!=extension && StringUtils.isNotEmpty(extension.installConfiguration)){
                    this.configuration = extension.installConfiguration as String
                }
            }

            if (this.configuration == null) {

                throw new GradleException("Install configuration is required to install the easy extension!")

            } else {
                JsonNode schemaNode = mapper.readTree(configurationSchemaFile)
                JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012)

                JsonSchema schema = schemaFactory.getSchema(schemaNode)

                JsonNode configNode = mapper.readTree(this.configuration)

                def validationResult = schema.validate(configNode)

                if(validationResult.isEmpty()){
                restClient.post(
                        path: "$easyApiBaseUrl/repository/$repositoryCode/extension/$extensionId/install",
                        body: this.configuration ? this.configuration : null,
                        query: ['async': 'false']
                )
                }else {
                    throw new GradleException("Install configuration is not valid against the schema configured in 'easy-installer-wizard.json'")
                }
            }


        } else {
            restClient.post(
                    path: "$easyApiBaseUrl/repository/$repositoryCode/extension/$extensionId/install",
                    query: ['async': 'false']
            )
        }


    }

}
