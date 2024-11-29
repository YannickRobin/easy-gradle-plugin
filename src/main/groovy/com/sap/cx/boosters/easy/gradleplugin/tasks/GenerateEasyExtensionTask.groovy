package com.sap.cx.boosters.easy.gradleplugin.tasks

import com.sap.cx.boosters.easy.gradleplugin.constants.EasyGradlePluginConstants
import com.sap.cx.boosters.easy.gradleplugin.util.ScaffoldingGenerator
import org.apache.commons.lang3.StringUtils
import org.codehaus.groovy.GroovyException
import org.gradle.api.DefaultTask
import org.gradle.api.internal.tasks.userinput.UserInputHandler
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

class GenerateEasyExtensionTask extends DefaultTask {

    static final String DEFAULT_EASY_EXTENSION_ID = 'helloworld'
    static final String DEFAULT_EASY_BASE_PACKAGE = 'com.mycompany.commerce.easy.extension'

    @Input
    @Optional
    String extensionId

    @Input
    @Optional
    String basePackage

    @Input
    @Optional
    String extensionDescription

    @Input
    @Optional
    String authors

    boolean noninteractive = false

    void init() {
        group = 'easy'
        description = 'generates an easy extension'
    }

    @Option(option = 'extensionId', description = 'Id of the extension')
    void setExtensionId(String extensionId) {
        this.extensionId = extensionId
    }

    @Option(option = 'basePackage', description = 'Base package of the extension')
    void setBasePackage(String basePackage) {
        this.basePackage = basePackage
    }

    @Option(option = 'extensionDescription', description = 'Description of the extension')
    void setExtensionDescription(String extensionDescription) {
        this.extensionDescription = extensionDescription
    }

    @Option(option = 'authors', description = 'Authors of the extension')
    void setAuthors(String authors) {
        this.authors = authors
    }

    @Option(option = 'noninteractive', description = 'Run in silent mode')
    void setNoninteractive(boolean noninteractive) {
        this.noninteractive = noninteractive
    }

    @Input
    boolean getNoninteractive() {
        return noninteractive
    }

    @TaskAction
    void generateEasyExtension() {
        if (project == project.gradle.rootProject) {
            this.initialize()
            def userInput = getServices().get(UserInputHandler.class)

            logger.info("noninteractive: " + noninteractive)

            this.extensionId = StringUtils.isEmpty(this.extensionId) ? userInput.askUser { it -> it.askQuestion("What is the extension id? ", DEFAULT_EASY_EXTENSION_ID) }.get() : this.extensionId

            if (!noninteractive) {
                this.basePackage = StringUtils.isEmpty(this.basePackage) ? userInput.askUser { it -> it.askQuestion("What is the extension base package? ", DEFAULT_EASY_BASE_PACKAGE) }.get() : this.basePackage
                this.extensionDescription = StringUtils.isEmpty(this.extensionDescription) ? userInput.askUser { it -> it.askQuestion("What is the extension description? ", "Lorem ipsum dollar") }.get() : this.extensionDescription
                this.authors = StringUtils.isEmpty(this.authors) ? userInput.askUser { it -> it.askQuestion("Who is/are the author(s) of this extension? ", "XYZ, ABC") }.get() : this.authors
            }

            this.extensionId = this.extensionId.replaceAll('[^a-zA-Z0-9-]', '')
            authors = authors.replaceAll(" ,", ",").replaceAll(", ", ",").replaceAll(",", "\",\"")
            def easyExtensionPackageName = this.basePackage + '.' + this.extensionId.replace('-', '').toLowerCase()
            def easyExtensionPackageFolder = easyExtensionPackageName.replaceAll('\\.', '/')
            def plugin = project.pluginManager.findPlugin('io.github.yannickrobin.easy-gradle-plugin')
            def parameters = [
                    'EASY_REPOSITORY_CODE'         : project.properties.get('sap.commerce.easy.repository.code'),
                    'EASY_GRADLE_PLUGIN_ID'        : plugin.id,
                    'EASY_GRADLE_PLUGIN_VERSION'   : EasyGradlePluginConstants.PLUGIN_VERSION,
                    'EASY_VERSION'                 : EasyGradlePluginConstants.DEFAULT_EASY_VERSION,
                    'EASY_EXTENSION_ID'            : this.extensionId,
                    'EASY_EXTENSION_BASE_PACKAGE'  : this.basePackage,
                    'EASY_EXTENSION_PACKAGE_NAME'  : easyExtensionPackageName,
                    'EASY_EXTENSION_PACKAGE_FOLDER': easyExtensionPackageFolder,
                    'EASY_EXTENSION_DESCRIPTION'   : this.extensionDescription,
                    'EASY_EXTENSION_AUTHORS'       : this.authors
            ]

            println("*** Configuration for extension generation ***")
            println("Easy version: '${parameters.EASY_VERSION}'")
            println("Extension Id: '${parameters.EASY_EXTENSION_ID}'")
            println("Extension Base Package: '${parameters.EASY_EXTENSION_BASE_PACKAGE}'")
            println("Extension Package Name: '${parameters.EASY_EXTENSION_PACKAGE_NAME}'")
            println("Extension Package Folder: '${parameters.EASY_EXTENSION_PACKAGE_FOLDER}'")
            println("Extension Description: '${parameters.EASY_EXTENSION_DESCRIPTION}'")
            println("Extension Authors: '${parameters.EASY_EXTENSION_AUTHORS}'")
            println("**********************************************")

            this.generateInternal(parameters)
            this.addProjectAsSubProjectToRoot()
        } else {
            println('Extension generation is applicable for root project (repository) only and not applicable to the easy extension project. Skipping it ...')
        }
    }

    void initialize() {
        this.initializeExtensionId()
        this.initializeBasePackage()
        this.initializeDescription()
        this.initializeAuthors()
    }

    void initializeExtensionId() {
        if (null == this.extensionId || this.extensionId.isBlank()) {
            def configuredExtensionId = project.gradle.startParameter.projectProperties.extensionId
            if (null != configuredExtensionId && !configuredExtensionId.isBlank()) {
                this.extensionId = configuredExtensionId
            }
        }
    }

    void initializeBasePackage() {
        if (null == this.basePackage || this.basePackage.isBlank()) {
            def configuredBasePackage = project.gradle.startParameter.projectProperties.basePackage
            if (null != configuredBasePackage && !configuredBasePackage.isBlank()) {
                this.basePackage = configuredBasePackage
            } else if (project.properties.containsKey('sap.commerce.easy.extension.base.package')) {
                this.basePackage = project.properties.get('sap.commerce.easy.extension.base.package')
            }
        }
    }

    void initializeDescription() {
        if (null == this.extensionDescription || this.extensionDescription.isBlank()) {
            def configuredDescription = project.gradle.startParameter.projectProperties.extensionDescription
            if (null != configuredDescription && !configuredDescription.isBlank()) {
                this.extensionDescription = configuredDescription
            }
        }
    }

    void initializeAuthors() {
        if (null == this.authors || this.authors.isBlank()) {
            def configuredAuthors = project.gradle.startParameter.projectProperties.authors
            if (null != configuredAuthors && !configuredAuthors.isBlank()) {
                this.authors = configuredAuthors
            }
        }
    }

    private void generateInternal(def parameters) {
        println("Generating new easy extension in current directory")

        def extensionDirectory = new File(project.projectDir, this.extensionId)
        if (extensionDirectory.exists()) {
            throw new GroovyException("A directory already exists in current directory with name '${this.extensionId}'. Please remove it and try again.")
        }
        if (extensionDirectory.mkdirs()) {
            logger.info("Easy extension directory '$extensionDirectory.absolutePath' created")
        } else {
            throw new GroovyException("Failed to create easy extension directory '$extensionDirectory.absolutePath'")
        }

        extensionDirectory = new File(project.projectDir, this.extensionId)
        URL resourceTemplateFolder = Thread.currentThread().contextClassLoader.getResource("templates/easy-extension")
        if (resourceTemplateFolder == null) {
            throw new IllegalArgumentException("Template folder not found")
        }

        def templateList = [
                'README.md.vm',
                '.gitignore.vm',
                'build.gradle.vm',
                'settings.gradle.vm',
                'gradle.properties.vm',
                'easy.json.vm',
                'src/main/resources/easy.properties.vm',
                'src/main/resources/impex/install/01-easyrest.impex.vm',
                'src/main/resources/impex/uninstall/01-easyrest.impex.vm',
                'src/main/groovy/EasyBeans.groovy.vm',
                'src/main/groovy/easy.gdsl',
                'src/main/groovy/Init.groovy',
                'src/main/groovy/__EASY_EXTENSION_PACKAGE_FOLDER__/service/HelloWorldService.groovy.vm',
                'src/main/groovy/__EASY_EXTENSION_PACKAGE_FOLDER__/controller/HelloWorldController.groovy.vm',
                'src/test/groovy/__EASY_EXTENSION_PACKAGE_FOLDER__/service/HelloWorldServiceTest.groovy.vm',
                'src/e2etest/groovy/__EASY_EXTENSION_PACKAGE_FOLDER__/controller/HelloWorldControllerTest.groovy.vm',
        ]

        ScaffoldingGenerator scaffoldingGenerator = new ScaffoldingGenerator("templates/easy-extension")
        scaffoldingGenerator.copyTemplateFiles(templateList, extensionDirectory.getPath(), parameters as Map<String, String>)

        println("Generated new easy extension [${this.extensionId}] in current directory.")
    }

    void addProjectAsSubProjectToRoot() {
        def settingsFile = project.rootProject.file('settings.gradle')
        def settingsContent = settingsFile.text

        if (!settingsContent.contains("\ninclude '${this.extensionId}' \n")) {
            settingsContent += "\ninclude '${this.extensionId}' \n"
            settingsFile.text = settingsContent
        }
    }
}
