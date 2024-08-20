package com.sap.cx.boosters.easy.gradleplugin.tasks

import groovyx.net.http.HttpResponseException
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

class ListExtensionsTask extends AbstractEasyTask {

    @Override
    void init() {
        super.init()
        description = "lists easy extensions of an easy repository"
    }

    @TaskAction
    void list() {
        init()

        try{
            def response = restClient.get(path: "$easyApiBaseUrl/repository/$repositoryCode/extensions")
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
