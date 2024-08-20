package com.sap.cx.boosters.easy.gradleplugin.tasks

import groovyx.net.http.HttpResponseException
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

class ReInstallExtensionTask extends AbstractEasyExtensionTask {

    @Override
    void init() {
        super.init()
        description = 're-installs/reloads an easy extension'
    }

    @TaskAction
    def reinstall() {
        init()

        try {
            def response = restClient.post(
                    path: "$easyApiBaseUrl/repository/$repositoryCode/extension/$extensionId/reinstall",
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
