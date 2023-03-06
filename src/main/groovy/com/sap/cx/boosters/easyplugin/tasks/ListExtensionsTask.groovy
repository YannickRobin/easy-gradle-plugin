package com.sap.cx.boosters.easyplugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input

import groovyx.net.http.RESTClient

import com.sap.cx.boosters.easyplugin.EasyPluginExtension
import com.sap.cx.boosters.easyplugin.EasyPluginUtil

class ListExtensionsTask extends DefaultTask {

    @Input
    EasyPluginExtension easyConfig

    @TaskAction
    def list() {

        EasyPluginUtil.displayEasyConfigInfo(easyConfig)
        println "List extensions..."

        def restClient = new RESTClient(easyConfig.baseUrl.get())
        restClient.ignoreSSLIssues()
        restClient.handler.failure = {def response, def data ->
            println "API call failed. HTTP status: $response.status";
            println "Error is $data";
        }
        restClient.handler.success = {def response, def data ->
            println "API call successfull. HTTP status: $response.status"
            println "$data"
        }        

        restClient.get(path: '/easyrest/easyapi/repository/' + easyConfig.repository.get() + '/extensions')
    }

}