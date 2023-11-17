package com.sap.cx.boosters.easy.gradleplugin.tasks

import org.gradle.api.tasks.TaskAction

class UpdateRepositoryTask extends AbstractEasyTask {


    @Override
    void init() {
        super.init()
        description = "updates the easy repository from remote location"
    }

    @TaskAction
    void update() {
        init()
        restClient.post(
                path: "$easyApiBaseUrl/repository/$repositoryCode/update",
                query: ['async': 'false']
        )
    }

}
