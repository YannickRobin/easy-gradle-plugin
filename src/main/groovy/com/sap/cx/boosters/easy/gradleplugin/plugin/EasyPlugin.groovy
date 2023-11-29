package com.sap.cx.boosters.easy.gradleplugin.plugin

import com.sap.cx.boosters.easy.gradleplugin.tasks.*
import com.sap.cx.boosters.easy.gradleplugin.util.CommerceExtensionUtil
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.testing.Test

class EasyPlugin implements Plugin<Project> {

    public static final String EXT_COMMERCE_PLATFORM_LIBRARIES = 'commercePlatformLibraries'

    public static final String PROP_COMMERCE_PLATFORM_HOME = 'sap.commerce.easy.platform.home'

    void apply(Project project) {
        project.plugins.apply(GroovyPlugin)

        project.plugins.withType(GroovyPlugin).configureEach {
            project.sourceSets.main.groovy.srcDirs += 'src/main/groovy'
            project.sourceSets.main.groovy.srcDirs += 'gensrc/main/groovy'
            project.sourceSets.test.groovy.srcDirs += 'src/test/groovy'
            project.sourceSets.test.groovy.srcDirs += 'src/e2etest/groovy'
            project.sourceSets.test.groovy.srcDirs += 'gensrc/test/groovy'
        }

        project.extensions.findByType(JavaPluginExtension).sourceCompatibility = JavaVersion.VERSION_17
        project.extensions.findByType(JavaPluginExtension).targetCompatibility = JavaVersion.VERSION_17

        project.repositories {
            mavenLocal()
            mavenCentral()
        }

        // add commerce libraries
        if (!project.hasProperty(PROP_COMMERCE_PLATFORM_HOME)) {
            project.logger.warn "no commerce platform home is set, specify ${PROP_COMMERCE_PLATFORM_HOME} in gradle.properties file"
        } else {

            project.extensions.add(
                    EXT_COMMERCE_PLATFORM_LIBRARIES,
                    project.files(CommerceExtensionUtil.buildPlatformClassPath(project.properties[PROP_COMMERCE_PLATFORM_HOME] as String))
            )
            project.dependencies.add('implementation', project.extensions.getByName(EXT_COMMERCE_PLATFORM_LIBRARIES))
        }

        project.dependencies.add('testImplementation', 'org.junit.vintage:junit-vintage-engine:5.10.1')

        project.tasks.withType(Test).configureEach {
            doFirst {
                systemProperty 'easyRestBaseUrl', "${project.properties['sap.commerce.easy.rest.base.url']}"
            }
            useJUnitPlatform()
            afterTest { desc, result ->
                logger.quiet "Executed test [${desc.className}.${desc.name}] with result: ${result.resultType}"
            }
        }

        project.tasks.register('easy-ext-gen', GenerateEasyExtensionTask) {
            group = 'easy'
            description = 'Generates an easy extension in current directory'
        }

        project.tasks.register('easy-class-gen', GenerateModelClassesTask) {
            group = 'easy'
            description = 'Generates an easy extension in current directory'
        }

        project.tasks.register('easy-update-repo', UpdateRepositoryTask) {
            group = 'easy'
            description = 'Updates the repository from remote location'
        }

        project.tasks.register('easy-ext-list', ListExtensionsTask) {
            group = 'easy'
            description = 'Lists the easy extensions of a repository'
        }

        project.tasks.register('easy-ext-install', InstallExtensionTask) {
            group = 'easy'
            description = 'Installs an easy extension'
        }

        project.tasks.register('easy-ext-reinstall', ReInstallExtensionTask) {
            group = 'easy'
            description = 'Re-installs/reloads an easy extension'
        }

        project.tasks.register('easy-ext-uninstall', UninstallExtensionTask) {
            group = 'easy'
            description = 'Uninstalls an easy extension'
        }

        project.tasks.register('easy-dump-classpath', DumpPlatformClassPathTask) {
            group = 'easy'
            description = 'Dumps the classpath'
        }

    }

}
