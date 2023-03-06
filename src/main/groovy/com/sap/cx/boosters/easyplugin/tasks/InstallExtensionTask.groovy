package com.sap.cx.boosters.easyplugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input

import groovyx.net.http.RESTClient

import com.sap.cx.boosters.easyplugin.EasyPluginExtension
import com.sap.cx.boosters.easyplugin.EasyPluginUtil

class InstallExtensionTask extends DefaultTask {

    @Input
    EasyPluginExtension easyConfig

    @TaskAction
    def install() {

        EasyPluginUtil.displayEasyConfigInfo(easyConfig)

        println "Install extension..."

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

        def response = restClient.post(path: '/easyrest/easyapi/repository/' + easyConfig.repository.get() + '/extension/' + easyConfig.extension.get() + '/install')
    }

}