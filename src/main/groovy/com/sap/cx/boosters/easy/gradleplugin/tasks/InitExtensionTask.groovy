package com.sap.cx.boosters.easy.gradleplugin.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input

import groovyx.net.http.RESTClient

import com.sap.cx.boosters.easy.gradleplugin.EasyPluginExtension
import com.sap.cx.boosters.easy.gradleplugin.EasyPluginUtil

class InitExtensionTask extends DefaultTask {

    @Input
    EasyPluginExtension easyConfig

    @TaskAction
    def init() {

        EasyPluginUtil.displayEasyConfigInfo(easyConfig)

        println "Init extension..."
        println "To be implemented..."
    }

}