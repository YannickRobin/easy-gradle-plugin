package com.sap.cx.boosters.easy.plugin

import com.sap.cx.boosters.easy.plugin.tasks.*
import org.gradle.api.Plugin
import org.gradle.api.Project

class SAPCommerceEasyPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        project.tasks.register("create-easy-extension", CreateEasyExtensionTask::class.java)

        project.tasks.register("generate-easytype-classes", GenerateModelClassesTask::class.java)

        project.tasks.register("update-easy-repository", UpdateRepositoryTask::class.java)

        project.tasks.register("list-easy-extensions", ListEasyExtensionsTask::class.java)

        project.tasks.register("install-easy-extension", InstallEasyExtensionTask::class.java)

        project.tasks.register("reinstall-easy-extension", ReInstallEasyExtensionTask::class.java)

        project.tasks.register("uninstall-easy-extension", UnInstallEasyExtensionTask::class.java)
    }

}
