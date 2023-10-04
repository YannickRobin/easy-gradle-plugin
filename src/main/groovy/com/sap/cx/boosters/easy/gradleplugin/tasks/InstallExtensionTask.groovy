package com.sap.cx.boosters.easy.gradleplugin.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input

import groovyx.net.http.RESTClient

import com.sap.cx.boosters.easy.gradleplugin.EasyPluginExtension
import com.sap.cx.boosters.easy.gradleplugin.EasyPluginUtil

class InstallExtensionTask extends AbstractEasyTask {

    @Input
    EasyPluginExtension easyConfig

    @TaskAction
    def install() {
        init("Install extension...", true)     
        restClient.post(
            path: '/easyrest/easyapi/repository/' + easyConfig.repository.get() + '/extension/' + easyConfig.extension.get() + '/install', 
            query:['async': 'false']
        )
    }

}