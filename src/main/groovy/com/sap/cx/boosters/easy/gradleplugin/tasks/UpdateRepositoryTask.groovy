package com.sap.cx.boosters.easy.gradleplugin.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input

import groovyx.net.http.RESTClient

import com.sap.cx.boosters.easy.gradleplugin.EasyPluginExtension
import com.sap.cx.boosters.easy.gradleplugin.EasyPluginUtil

class UpdateRepositoryTask extends AbstractEasyTask {

    @Input
    EasyPluginExtension easyConfig

    @TaskAction
    def update() {
        init("Update repository...", true)
        restClient.post(
            path: '/easyrest/easyapi/repository/' + easyConfig.repository.get() + '/update', 
            query:['async': 'false']
        )
    }

}