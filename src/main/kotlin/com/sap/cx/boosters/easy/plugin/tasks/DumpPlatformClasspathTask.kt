package com.sap.cx.boosters.easy.plugin.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

abstract class DumpPlatformClasspathTask : DefaultTask() {

    @Input
    var commercePlatformHome : String = ""

    @Input
    var commercePlatformLibraries : String = ""

    init {
        group = "easy"
        description = "Dumps platform classpath"
    }

    @TaskAction
    fun dumpCommerceClasspath(){

    }

}
