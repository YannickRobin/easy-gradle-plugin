package com.sap.cx.boosters.easy.gradleplugin.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input

import groovyx.net.http.RESTClient
import groovy.json.JsonOutput

import com.sap.cx.boosters.easy.gradleplugin.EasyPluginExtension
import com.sap.cx.boosters.easy.gradleplugin.EasyPluginUtil

abstract class AbstractEasyTask extends DefaultTask {
  
    protected String apiKey;
    protected RESTClient restClient;

    void init(String initMessage) {
        init(initMessage, false)
    }

    void init(String initMessage, boolean eventEnabled) {
        displayEasyConfigInfo()
        println initMessage
        setRestClient(eventEnabled)
    }       

    private void displayEasyConfigInfo() {
        println "Welcome to Easy Gradle Plugin\n"
        println "SAP Commerce Base URL: ${easyConfig.baseUrl.get()}"
        println "Repository: ${easyConfig.repository.get()}"
        println "Extension: ${easyConfig.extension.get()}\n"
    }

    private void setRestClient(boolean eventEnabled) {
        restClient = new RESTClient(easyConfig.baseUrl.get())
        restClient.headers['accept'] = 'application/json'

        apiKey = easyConfig.apiKey.get()
        if (apiKey == null || apiKey === '')
            throw new IllegalArgumentException("Environment variable EASY_API_KEY not set")

        restClient.headers['X-API-KEY'] = apiKey
        restClient.ignoreSSLIssues()

        restClient.handler.failure = {def response, def data ->
            println "API call failed. HTTP status: $response.status";
            def jsonData = JsonOutput.toJson(data)
            def prettyData = JsonOutput.prettyPrint(jsonData)                      
            println "$prettyData";
        }

        restClient.handler.success = {def response, def data ->
            println "API call successfull. HTTP status: $response.status"   
            def jsonData = JsonOutput.toJson(data)
            def prettyData = JsonOutput.prettyPrint(jsonData)
            println "$prettyData"

        }

    }

}