package com.sap.cx.boosters.easy.gradleplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

import com.sap.cx.boosters.easy.gradleplugin.tasks.*

class EasyPlugin implements Plugin<Project> {

    public static final String EXT_COMMERCE_PLATFORM_LIBRARIES = 'commercePlatformLibraries'

    public static final String PROP_COMMERCE_PLATFORM_HOME = 'commercePlatformHome'

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
        if (!project.hasProperty(PROP_COMMERCE_PLATFORM_HOME)) {
            project.logger.warn "no commerce platform home is set, specify commercePlatformHome in gradle.properties file"
        }

        project.extensions.add(
                EXT_COMMERCE_PLATFORM_LIBRARIES,
                project.files(CommerceExtensionUtil.buildPlatformClassPath(project.properties[PROP_COMMERCE_PLATFORM_HOME] as String))
        )

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

        project.tasks.register('dumpPlatformClassPath', DumpPlatformClassPathTask) {
            group 'Easy'
            description 'Dump Platform Classpath'
        }

    }

}