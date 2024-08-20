package com.sap.cx.boosters.easy.gradleplugin.tasks

import groovyx.net.http.HttpResponseException
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

class UninstallExtensionTask extends AbstractEasyExtensionTask {

    @Override
    void init() {
        super.init()
        description = 'uninstalls an easy extension'
    }

    @TaskAction
    def uninstall() {
        init()
        try {
            def response = restClient.post(
                    path: "$easyApiBaseUrl/repository/$repositoryCode/extension/$extensionId/uninstall",
                    query: [async: false]

            )
            printResponse(response)
        } catch (HttpResponseException exception) {
            if(exception.response){
                throw new GradleException(String.format("[Error] - %s", exception.response.data.errors[0].message))
            }else{
                throw new GradleException("Installation failed. Refer to logs for error details.")
            }
        }
    }

}
