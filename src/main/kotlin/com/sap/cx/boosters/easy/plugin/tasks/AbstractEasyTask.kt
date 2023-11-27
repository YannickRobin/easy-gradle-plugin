package com.sap.cx.boosters.easy.plugin.tasks

import groovy.json.JsonOutput
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient
import org.apache.commons.lang3.StringUtils
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal

abstract class AbstractEasyTask() : DefaultTask() {

    @Input
    var easyApiBaseUrl: String = ""

    @Input
    var easyApiKey: String = ""

    @Input
    var repositoryCode: String = ""

    @Internal
    protected lateinit var restClient: RESTClient

    init {
        group = "easy"
        setEasyApiBaseUrl()
        setEasyApiKey()
        setRepositoryCode()
        initializeRestClient()
    }

    private fun setRepositoryCode() {
        if (StringUtils.isEmpty(repositoryCode)) {
            val userInput = project.gradle.startParameter.projectProperties["repositoryCode"]
            repositoryCode = if (!userInput.isNullOrBlank()) {
                userInput
            } else if (project.properties.contains("sap.commerce.easy.repository.code")) {
                project.property("sap.commerce.easy.repository.code") as String
            } else {
                logger.info("Enter the easy repository code for the extension: ")
                val input = readlnOrNull().toString()
                input.ifBlank {
                    throw GradleException("Easy repository code is required for install-easy-extension task")
                }
            }
        }
    }

    private fun setEasyApiKey() {
        if (StringUtils.isEmpty(easyApiKey)) {
            val userInput = project.gradle.startParameter.projectProperties["easyApiKey"]
            easyApiKey = if (!userInput.isNullOrBlank()) {
                userInput
            } else if (project.properties.contains("sap.commerce.easy.api.key")) {
                project.property("sap.commerce.easy.api.key") as String
            } else {
                logger.info("Enter the easy API Key: ")
                val input = readlnOrNull().toString()
                input.ifBlank {
                    throw GradleException("Easy API Key is required for easy tasks")
                }
            }
        }
    }

    private fun setEasyApiBaseUrl() {
        if (StringUtils.isEmpty(easyApiBaseUrl)) {
            val userInput = project.gradle.startParameter.projectProperties["easyApiBaseUrl"]
            easyApiBaseUrl = if (!userInput.isNullOrBlank()) {
                userInput
            } else if (project.properties.contains("sap.commerce.easy.api.base.url")) {
                project.property("sap.commerce.easy.api.base.url") as String
            } else {
                logger.info("Enter the Base Easy API URL: ")
                val input = readlnOrNull().toString()
                input.ifBlank {
                    throw GradleException("Base Easy API URL is required for easy tasks")
                }
            }
        }
    }

    private fun initializeRestClient() {
        if (StringUtils.isEmpty(easyApiBaseUrl)) {
            throw GradleException("Easy API url is not configured.")
        }

        if (StringUtils.isEmpty(easyApiKey)) {
            throw GradleException("Easy API key is not configured.")
        }

        restClient = RESTClient(easyApiBaseUrl)
        restClient.headers += mapOf(
            "accept" to groovyx.net.http.ContentType.JSON,
            "X-API-KEY" to easyApiKey
        )
        restClient.ignoreSSLIssues()
    }

    private fun handleResponse(response: HttpResponseDecorator) {
        if (response.status == 200) {
            println("API request executed successfully. HTTP status: ${response.statusLine}")
        }
        val jsonData = JsonOutput.toJson(response.data)
        val prettyData = JsonOutput.prettyPrint(jsonData)
        println(prettyData)
    }

    @Internal
    protected fun getAsyncQueryParameters(): Map<String, String> {
        return mapOf("async" to "true")
    }

    protected fun submitEvent(path: String) {
        val args = mutableMapOf("path" to path, "query" to getAsyncQueryParameters())
        try {
            val response = restClient.post(args) as HttpResponseDecorator
            handleResponse(response)
        } catch (exception: HttpResponseException) {
            println("API request failed. HTTP Status: ${exception.response.statusLine}")
            val jsonData = JsonOutput.toJson(exception.response.data)
            val prettyData = JsonOutput.prettyPrint(jsonData)
            println(prettyData)
        }
    }

    protected fun executeGetRequest(path: String) {
        val args = mutableMapOf("path" to path)
        try {
            val response = restClient.get(args) as HttpResponseDecorator
            handleResponse(response)
        } catch (exception: HttpResponseException) {
            println("API request failed. HTTP Status: ${exception.response.statusLine}")
            val jsonData = JsonOutput.toJson(exception.response.data)
            val prettyData = JsonOutput.prettyPrint(jsonData)
            println(prettyData)
        }
    }

}
