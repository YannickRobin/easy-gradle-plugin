package com.sap.cx.boosters.easyplugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import groovyx.net.http.RESTClient

import com.sap.cx.boosters.easyplugin.tasks.*

class EasyPlugin implements Plugin<Project> {

    void apply(Project project) {
        def EasyPluginExtension myEasyConfig = project.extensions.create('easyConfig', EasyPluginExtension)

        myEasyConfig.baseUrl.set(System.env.EASY_BASE_URL ?: 'https://localhost:9002')
        myEasyConfig.repository.set(System.env.EASY_REPOSITORY ?: 'easy-extension-samples')
        myEasyConfig.extension.set(project.name)

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