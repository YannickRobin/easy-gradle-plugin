package com.sap.cx.boosters.easy.gradleplugin.tasks


import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import com.sap.cx.boosters.easy.gradleplugin.plugin.extension.EasyPluginExtension

class ListExtensionsTask extends AbstractEasyTask {

    @Override
    void init() {
        super.init()
        description = "lists easy extensions of an easy repository"
    }

    @TaskAction
    void list() {
        init()
        restClient.get(path: "$easyApiBaseUrl/repository/$repositoryCode/extensions")
    }

}
