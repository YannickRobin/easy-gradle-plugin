package com.sap.cx.boosters.easy.gradleplugin

class EasyPluginUtil {

    static void displayEasyConfigInfo(EasyPluginExtension easyConfig)
    {
        println "Welcome to Easy Gradle Plugin\n"
        println "SAP Commerce Base URL: ${easyConfig.baseUrl.get()}"
        println "Repository: ${easyConfig.repository.get()}"
        println "Extension: ${easyConfig.extension.get()}\n"
    }

}