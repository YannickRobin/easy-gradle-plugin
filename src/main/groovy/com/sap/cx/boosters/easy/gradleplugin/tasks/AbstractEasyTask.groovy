package com.sap.cx.boosters.easy.gradleplugin.tasks

import com.fasterxml.jackson.databind.ObjectMapper
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional

abstract class AbstractEasyTask extends DefaultTask {

    @Input
    @Optional
    String easyApiKey

    @Input
    @Optional
    String easyApiBaseUrl

    @Input
    @Optional
    String repositoryCode

    @Internal
    RESTClient restClient

    void init() {
        group = "easy"
        this.initializeEasyApiKey()
        this.initializeEasyApiBaseUrl()
        this.initializeRepositoryCode()
        this.initRestClient()
    }

    void initializeEasyApiKey() {
        if (null == this.easyApiKey || this.easyApiKey.isBlank()) {
            String configuredApiKey = project.gradle.startParameter.projectProperties.easyApiKey
            if (null != configuredApiKey && !configuredApiKey.isBlank()) {
                this.easyApiKey = configuredApiKey
            } else if (project.properties.containsKey('sap.commerce.easy.api.key')) {
                this.easyApiKey = project.properties.get('sap.commerce.easy.api.key')
            } else {
                throw new GradleException('Easy API Key is required for easy tasks')
            }
        }
    }

    void initializeEasyApiBaseUrl() {
        if (null == this.easyApiBaseUrl || this.easyApiBaseUrl.isBlank()) {
            String configuredEasyApiBaseUrl = project.gradle.startParameter.projectProperties.easyApiBaseUrl
            if (null != configuredEasyApiBaseUrl && !configuredEasyApiBaseUrl.isBlank()) {
                this.easyApiBaseUrl = configuredEasyApiBaseUrl
            } else if (project.properties.containsKey('sap.commerce.easy.api.base.url')) {
                this.easyApiBaseUrl = project.properties.get('sap.commerce.easy.api.base.url')
            } else {
                throw new GradleException('Easy API url is required for easy tasks')
            }
        }
    }

    void initializeRepositoryCode() {
        if (null == this.repositoryCode || this.repositoryCode.isBlank()) {
            String configuredRepositoryCode = project.gradle.startParameter.projectProperties.repositoryCode
            if (null != configuredRepositoryCode && !configuredRepositoryCode.isBlank()) {
                this.repositoryCode = configuredRepositoryCode
            } else if (project.properties.containsKey('sap.commerce.easy.repository.code')) {
                this.repositoryCode = project.properties.get("sap.commerce.easy.repository.code")
            } else {
                throw new GradleException('Repository Code is required for executing easy events.')
            }
        }
    }

    void initRestClient() {
        restClient = new RESTClient(this.easyApiBaseUrl)
        restClient.headers.'accept' = 'application/json'

        restClient.headers.'x-api-key' = this.easyApiKey
        if (project.properties.containsKey("sap.commerce.easy.api.key.client")) {
            restClient.headers.'client' = project.properties.get("sap.commerce.easy.api.key.client")
        }
        restClient.ignoreSSLIssues()

    }

    protected static void printResponse(def res) {
        def response = res as HttpResponseDecorator
        if (response.status == 200) {
            println 'API executed successfully.'
            def mapper = new ObjectMapper()
            println "Response is: ${mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response.data)}"
        } else if (response.status == 404) {
            throw new GradleException("API not available. Please install [easyapi] easy extension from [easy-extension-samples] repository.")
        } else {
            throw new GradleException("API execution failed. ${response.data}")
        }
    }

}
