package com.sap.cx.boosters.easyplugin.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input

import groovyx.net.http.RESTClient

import com.sap.cx.boosters.easyplugin.EasyPluginExtension
import com.sap.cx.boosters.easyplugin.EasyPluginUtil

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