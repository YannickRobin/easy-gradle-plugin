package com.sap.cx.boosters.easy.gradleplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

import com.sap.cx.boosters.easy.gradleplugin.tasks.*

class EasyPlugin implements Plugin<Project> {

    void apply(Project project) {

        def myEasyConfig = project.extensions.create('easyConfig', EasyPluginExtension)

        myEasyConfig.baseUrl.set(System.env.EASY_BASE_URL ?: 'https://localhost:9002')
        myEasyConfig.repository.set(System.env.EASY_REPOSITORY ?: 'easy-extension-samples')
        myEasyConfig.apiKey.set(System.env.EASY_API_KEY ?: '')
        myEasyConfig.extension.set(project.name)

        // add source folder for models
        if (project.plugins.hasPlugin('groovy')) {
            project.sourceSets.main.groovy.srcDirs += 'gensrc/models/groovy'
        }

        // add commerce libraries
        if (project.hasProperty('commercePlatformHome')) {
            def commercePlatformHome = EasyPluginUtil.resolveHome(project.properties['commercePlatformHome'] as String)
            project.extensions.add('commercePlatformLibraries', project.files(EasyPluginUtil.buildPlatformClassPath(commercePlatformHome)))
        }

        project.tasks.register('updateEasyRepository', UpdateRepositoryTask) {
            group 'Easy'
            description 'Update repository'            
            easyConfig = myEasyConfig    
        }
        
        project.tasks.register('listEasyExtensions', ListExtensionsTask) {
            group 'Easy'
            description 'List extensions'
            easyConfig = myEasyConfig      
        }
        project.tasks.register('initEasyExtension', InitExtensionTask) {
            group 'Easy'
            description 'Init extension'            
            easyConfig = myEasyConfig      
        }        
        project.tasks.register('installEasyExtension', InstallExtensionTask) {
            group 'Easy'
            description 'Install extension'            
            easyConfig = myEasyConfig      
        }
        project.tasks.register('uninstallEasyExtension', UninstallExtensionTask) {
            group 'Easy'
            description 'Uninstall extension'            
            easyConfig = myEasyConfig      
        }

    }

}