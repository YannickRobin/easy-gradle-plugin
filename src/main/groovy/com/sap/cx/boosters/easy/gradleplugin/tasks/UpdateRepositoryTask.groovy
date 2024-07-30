package com.sap.cx.boosters.easy.gradleplugin.tasks

import groovyx.net.http.HttpResponseException
import org.gradle.api.GradleException
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

        try{
            def response = restClient.post(
                    path: "$easyApiBaseUrl/repository/$repositoryCode/update",
                    query: [async: false]

            )
            printResponse(response)
        }catch (HttpResponseException exception){
            if(exception.response){
                throw new GradleException(String.format("[Error] - %s", exception.response.data.errors[0].message))
            }else{
                throw new GradleException("Installation failed. Refer to logs for error details.")
            }
        }
    }

}
