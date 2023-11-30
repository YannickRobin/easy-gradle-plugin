package com.sap.cx.boosters.easy.gradleplugin.tasks

import com.sap.cx.boosters.easy.gradleplugin.data.EasyTypes
import com.sap.cx.boosters.easy.gradleplugin.util.ScaffoldingGenerator

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.io.FileType
import org.codehaus.groovy.GroovyException

import org.gradle.api.internal.tasks.userinput.UserInputHandler;
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

class GenerateEasyExtensionTask extends DefaultTask {

    static final String DEFAULT_EASY_EXTENSION_ID = 'helloworld'
    static final String DEFAULT_EASY_BASE_PACKAGE = 'com.mycompany.commerce.easy.extension'     
    static final String DEFAULT_EASY_VERSION = '0.3'

    @Input
    @Optional
    String extensionId

    @Input
    @Optional
    String basePackage

    @Internal
    String extensionDescription = ''

    @Internal
    String authors = ''

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

    @Option(option = 'noninteractive', description = 'Run in silent mode')
    void setNoninteractive(boolean noninteractive) { 
        this.noninteractive = noninteractive
    }

    @Input
    public boolean getNoninteractive() {
        return noninteractive;
    }

    @TaskAction
    void generateEasyExtension() {
        if (project == project.gradle.rootProject) {
            this.initializeExtensionId()
            this.initializeBasePackage()            

            logger.info("noninteractive: " + noninteractive)

            if (!noninteractive)
            {
                def userInput = getServices().get(UserInputHandler.class);
                this.extensionId = userInput.askQuestion("What is the extension id? ", this.extensionId)
                this.basePackage = userInput.askQuestion("What is the extension base package? ", this.basePackage)
                this.extensionDescription = userInput.askQuestion("What is the extension description?", this.extensionDescription)                
                this.authors = userInput.askQuestion("What is/are the author(s) of this extension (use comma separator)", this.authors)
            }

            this.extensionId = this.extensionId.replaceAll('[^a-zA-Z0-9-]', '')
            authors = authors.replaceAll(" ,", ",").replaceAll(", ", ",").replaceAll(",", "\",\"")
            def easyExtensionPackageName = this.basePackage + '.' + this.extensionId.replace('-', '').toLowerCase()
            def easyExtensionPackageFolder = easyExtensionPackageName.replaceAll('\\.', '/')

            def parameters = [
                    'EASY_GRADLE_PLUGIN_GROUP_ID'  : project.group.toString(),
                    'EASY_GRADLE_PLUGIN_NAME'      : project.name,
                    'EASY_GRADLE_PLUGIN_VERSION'   : project.version.toString(),
                    'EASY_VERSION'                 : DEFAULT_EASY_VERSION,                    
                    'EASY_EXTENSION_ID'            : this.extensionId,
                    'EASY_EXTENSION_BASE_PACKAGE'  : this.basePackage,
                    'EASY_EXTENSION_PACKAGE_NAME'  : easyExtensionPackageName,
                    'EASY_EXTENSION_PACKAGE_FOLDER': easyExtensionPackageFolder,                 
                    'EASY_EXTENSION_DESCRIPTION'   : this.extensionDescription,
                    'EASY_EXTENSION_AUTHORS'       : this.authors
            ]

            logger.info("*** Configuration for extension generation ***")
            logger.info("Repository Directory: '${project.projectDir}'")
            logger.info("Easy version: '${parameters.EASY_VERSION}'")
            logger.info("Extension Id: '${parameters.EASY_EXTENSION_ID}'")
            logger.info("Extension Base Package: '${parameters.EASY_EXTENSION_BASE_PACKAGE}'")            
            logger.info("Extension Package Name: '${parameters.EASY_EXTENSION_PACKAGE_NAME}'")
            logger.info("Extension Package Folder: '${parameters.EASY_EXTENSION_PACKAGE_FOLDER}'")
            logger.info("Extension Description: '${parameters.EASY_EXTENSION_PACKAGE_FOLDER}'")
            logger.info("Extension Authors: '${parameters.EASY_EXTENSION_PACKAGE_FOLDER}'")
            logger.info("******")

            this.generateInternal(parameters)
            this.addProjectAsSubProjectToRoot()
        } else {
            println('Extension generation is applicable for root project (repository) only and not applicable to the easy extension project. Skipping it ...')
        }
    }

    void initializeExtensionId() {
        if (null == this.extensionId || this.extensionId.isBlank()) {
            def configuredExtensionId = project.gradle.startParameter.projectProperties.extensionId
            if (null != configuredExtensionId && !configuredExtensionId.isBlank()) {
                this.extensionId = configuredExtensionId
            } else {
                this.extensionId = DEFAULT_EASY_EXTENSION_ID
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
            } else {
                this.basePackage = DEFAULT_EASY_BASE_PACKAGE
            }
        }
    }

    private void generateInternal(Map<String, String> parameters) {
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
            throw new IllegalArgumentException("Template folder not found");
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
        ];

        ScaffoldingGenerator scaffoldingGenerator = new ScaffoldingGenerator("templates/easy-extension")
        scaffoldingGenerator.copyTemplateFiles(templateList, extensionDirectory.getPath(), parameters)
    }

    private void createEasyTypeDescriptor(File extensionDirectory) {
        logger.info("creating easy types descriptor 'easytypes.json'")
        File easyTypesDescriptor = new File(extensionDirectory, "easytypes.json")
        this.createFile(easyTypesDescriptor)

        EasyTypes easyTypes = new EasyTypes()

        def objectMapper = new ObjectMapper()
        easyTypesDescriptor.text = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(easyTypes)
        logger.info("created easy types descriptor 'easytypes.json'")
    }

    private void createFile(File file) {
        if (file.exists()) {
            throw new GroovyException("A file already exists with name '$file.name'. Please remove it and try again.")
        }
        if (file.createNewFile()) {
            logger.info("File '$file.absolutePath' created.")
        } else {
            throw new GroovyException("Failed to create file '$file.absolutePath'")
        }
    }

    void addProjectAsSubProjectToRoot(){
        def settingsFile = project.rootProject.file('settings.gradle')
        def settingsContent = settingsFile.text

        if (!settingsContent.contains("\ninclude '${this.extensionId}' \n")) {
            settingsContent += "\ninclude '${this.extensionId}' \n"
            settingsFile.text = settingsContent
        }
    }    

}
