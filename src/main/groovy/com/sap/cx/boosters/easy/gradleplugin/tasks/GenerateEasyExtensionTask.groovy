package com.sap.cx.boosters.easy.gradleplugin.tasks

import com.fasterxml.jackson.databind.ObjectMapper
import com.sap.cx.boosters.easy.gradleplugin.data.EasyExtension
import com.sap.cx.boosters.easy.gradleplugin.data.EasyTypes
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine
import org.codehaus.groovy.GroovyException
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import groovy.io.FileType

import java.util.zip.ZipFile
import java.util.zip.ZipEntry

class GenerateEasyExtensionTask extends DefaultTask {

    @Input
    @Optional
    String easyExtensionId

    @Input
    @Optional
    String easyVersion

    @Input
    @Optional
    String basePackage

    void init() {
        group = 'easy'
        description = 'generates an easy extension'
    }

    @TaskAction
    void generateEasyExtension() {
        println("Generating new easy extension in current directory")
        this.initializeBasePackage()        
        this.initializeExtensionId()
        this.initializeEasyVersion()
        def easyExtensionPackageName = this.basePackage + '.' + this.easyExtensionId
        def easyExtensionPackageFolder = easyExtensionPackageName.replaceAll('\\.', '/')

        logger.info("Configuration for extension generation are:")
        logger.info("Repository Directory: '${project.projectDir}'")
        logger.info("Base Package: '${this.basePackage}'")        
        logger.info("Extension Id: '${this.easyExtensionId}'")
        logger.info("Easy Version: '${this.easyVersion}'")
        logger.info("Extension Package Name: '${easyExtensionPackageName}'")
        logger.info("Extension Package Folder: '${easyExtensionPackageFolder}'")

        def parameters = [
                'EASY_GRADLE_PLUGIN_GROUP_ID': project.group.toString(),
                'EASY_GRADLE_PLUGIN_NAME'    : project.name,
                'EASY_GRADLE_PLUGIN_VERSION' : project.version.toString(),
                'EASY_API_BASE_URL'          : '',
                'EASY_API_KEY'               : '',
                'EASY_EXTENSION_ID'          : this.easyExtensionId,
                'EASY_EXTENSION_DESCRIPTION' : '',
                'EASY_EXTENSION_AUTHORS'      : '',
                'EASY_VERSION'               : this.easyVersion,
                'EASY_EXTENSION_BASE_PACKAGE': this.basePackage,
                'EASY_EXTENSION_PACKAGE_NAME': easyExtensionPackageName,
                'EASY_EXTENSION_PACKAGE_FOLDER': easyExtensionPackageFolder,
                'EASY_REPOSITORY_CODE'       : ''
        ]

        this.generateInternal(parameters)
    }

    void initializeExtensionId() {
        if (null == this.easyExtensionId || this.easyExtensionId.isBlank()) {
            def configuredExtensionId = project.gradle.startParameter.projectProperties.easyExtensionId
            if (null != configuredExtensionId && !configuredExtensionId.isBlank()) {
                this.easyExtensionId = configuredExtensionId
            } else {
                throw new GradleException("Property easyExtensionId is required to create easy extension")
            }
        }
    }

    void initializeEasyVersion() {
        if (null == this.easyVersion || this.easyVersion.isBlank()) {
            def configuredEasyVersion = project.gradle.startParameter.projectProperties.easyVersion
            if (null != configuredEasyVersion && !configuredEasyVersion.isBlank()) {
                this.easyVersion = configuredEasyVersion
            } else if (project.properties.containsKey('sap.commerce.easy.extension.easy.version')) {
                this.easyVersion = project.properties.get('sap.commerce.easy.extension.easy.version')
            } else {
                this.easyVersion = '0.2'
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
                this.basePackage = 'com.sap.cx.boosters.easy.extension'
            }
        }
    }
    
    private void generateInternal(Map<String, String> parameters) {
        def extensionDirectory = new File(project.projectDir, this.easyExtensionId)
        if (extensionDirectory.exists()) {
            throw new GroovyException("A directory already exists in current directory with name '${this.easyExtensionId}'. Please remove it and try again.")
        }
        if (extensionDirectory.mkdirs()) {
            logger.info("Easy extension directory '$extensionDirectory.absolutePath' created")
        } else {
            throw new GroovyException("Failed to create easy extension directory '$extensionDirectory.absolutePath'")
        }

        extensionDirectory = new File(project.projectDir, this.easyExtensionId)
        java.net.URL resourceTemplateFolder = Thread.currentThread().contextClassLoader.getResource("templates/easy-extension")
        if (resourceTemplateFolder == null) {
            throw new IllegalArgumentException("Template folder not found");
        }

        def templateList = [
            'README.md.vm',
            '.gitignore',
            'build.gradle.vm',
            'settings.gradle.vm',
            'gradle.properties.vm',
            'easy.json.vm',
            'easy.properties.vm',
            'impex/install/01-easyrest.impex.vm',
            'impex/uninstall/01-easyrest.impex.vm',
            'src/main/groovy/EasyBeans.groovy.vm',
            'src/main/groovy/easy.gdsl',
            'src/main/groovy/Init.groovy',            
            'src/main/groovy/__EASY_EXTENSION_PACKAGE_FOLDER__/service/HelloWorldService.groovy.vm',
            'src/main/groovy/__EASY_EXTENSION_PACKAGE_FOLDER__/controller/HelloWorldController.groovy.vm',
            'src/test/groovy/__EASY_EXTENSION_PACKAGE_FOLDER__/service/HelloWorldServiceTest.groovy.vm',
            'src/e2etest/groovy/__EASY_EXTENSION_PACKAGE_FOLDER__/controller/HelloWorldControllerTest.groovy.vm',
          ];

        copyTemplateFiles(templateList, extensionDirectory.getPath(), parameters)
    }

    private void createEasyTypeDescriptor(File extensionDirectory) {
        logger.info("creating easy types descriptor 'easytypes.json' for extension '${this.easyExtensionId}'")
        File easyTypesDescriptor = new File(extensionDirectory, "easytypes.json")
        this.createFile(easyTypesDescriptor)

        EasyTypes easyTypes = new EasyTypes()

        def objectMapper = new ObjectMapper()
        easyTypesDescriptor.text = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(easyTypes)
        logger.info("created easy types descriptor 'easytypes.json' for extension '${this.easyExtensionId}'")
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

    private void copyTemplateDirectory(String templateDir, String destFolder, Map<String, String> parameters) {
        def list = []

        def dir = new File("path_to_parent_dir")
        dir.eachFileRecurse (FileType.FILES) { file ->
        list << file
        }

        //Afterwards the list variable contains all files (java.io.File) of 
        //the given directory and its subdirectories
        list.each {
            println it.path
        }
    }

    private void copyTemplateFiles(List templateList, String destFolder, Map<String, String> parameters) {
        templateList.each{ String template -> 
            copyTemplateFile(template, destFolder, parameters)
        }
    }

    public void copyTemplateFile(String template, String destFolder, Map<String, String> parameters) {        
        logger.info("copying resource '${template}' for extension '${this.easyExtensionId}'")
        
        InputStream templateStream = Thread.currentThread().contextClassLoader.getResourceAsStream("templates/easy-extension/${template}")
        if (templateStream == null) {
            logger.error("Resource template '${template}' not found")
            return;
        }

        if(isDirectory(template)) { 
           logger.error("Resource template '${template}' is a directory and this is not supported")
           return;
        }

        //Let's parse template for __MY_PARAMETER__ to replace by the parameter value
        parameters.each { key, value ->
            template = template.replaceAll("__${key}__", value)
        }

        if (template.endsWith('.vm'))
        {
            template = template.substring(0, template.lastIndexOf('.vm'))
            Path targetFile = Paths.get(destFolder).resolve(template)
            copyVelocityResource(templateStream, targetFile, parameters)
        }
        else {
            Path targetFile = Paths.get(destFolder).resolve(template)
            copyResource(templateStream, targetFile)
        }
        templateStream.close()

        logger.info("copied resource '${template}' for extension '${this.easyExtensionId}'")
    }

    private boolean isDirectory(String template)
    {
        String file = Thread.currentThread().contextClassLoader.getResource("templates/easy-extension/${template}").getFile();
		int bangIndex = file.indexOf('!');
		String jarPath = file.substring(bangIndex + 2);
		file = new URL(file.substring(0, bangIndex)).getFile();
		ZipFile zip = new ZipFile(file);
		ZipEntry entry = zip.getEntry(jarPath);
        boolean isDirectory = entry.isDirectory();
        return entry.isDirectory()
    }

    private void copyResource(InputStream templateStream, Path targetPath) {
        Files.createDirectories(targetPath.getParent());
        targetPath.toFile().append(templateStream)
    }   

    private void copyVelocityResource(InputStream templateStream, Path targetPath, Map<String, String> tokenReplacements) {
        def velocityEngine = new VelocityEngine()
        velocityEngine.init()

        def contentTemplate = new Scanner(templateStream).useDelimiter("\\A").next()
        def velocityContext = new VelocityContext()

        tokenReplacements.each {
            velocityContext.put(it.key, it.value)
        }

        def result = new StringWriter()
        velocityEngine.evaluate(velocityContext, result, 'replaceTokens', contentTemplate)

        Files.createDirectories(targetPath.getParent());
        def targetFile = targetPath.toFile()

        targetFile.text = result.toString()
    }

}
