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

    @TaskAction
    void generateEasyExtension() {
        println("Generating new easy extension in current directory")
        this.initializeExtensionId()
        this.initializeEasyVersion()
        this.initializeBasePackage()
        logger.info("Configuration for extension generation are:")
        logger.info("Repositry Directory: '${project.projectDir}'")
        logger.info("Extension Id: '${this.easyExtensionId}'")
        logger.info("Easy Version: '${this.easyVersion}'")
        logger.info("Base Package: '${this.basePackage}'")

        this.generateInternal()
    }

    private void generateInternal() {
        def extensionDirectory = new File(project.projectDir, this.easyExtensionId)
        if (extensionDirectory.exists()) {
            throw new GroovyException("A directory already exists in current directory with name '${this.easyExtensionId}'. Please remove it and try again.")
        }
        if (extensionDirectory.mkdirs()) {
            logger.info("Easy extension directory '$extensionDirectory.absolutePath' created")
        } else {
            throw new GroovyException("Failed to create easy extension directory '$extensionDirectory.absolutePath'")
        }

        def mainSourceDirectory = Files.createDirectories(Path.of(extensionDirectory.absolutePath, "src", "main", "groovy"))
        def mainGeneratedSourceDirectory = Files.createDirectories(Path.of(extensionDirectory.absolutePath, "gensrc", "main", "groovy"))
        def testSourceDirectory = Files.createDirectories(Path.of(extensionDirectory.absolutePath, "src", "test", "groovy"))
        def testGeneratedSourceDirectory = Files.createDirectories(Path.of(extensionDirectory.absolutePath, "gensrc", "test", "groovy"))

        Files.createDirectories(Path.of(mainSourceDirectory.toString(), this.basePackage.replace('.', '/')))
        Files.createDirectories(Path.of(mainGeneratedSourceDirectory.toString(), this.basePackage.replace('.', '/')))
        Files.createDirectories(Path.of(testSourceDirectory.toString(), this.basePackage.replace('.', '/')))
        Files.createDirectories(Path.of(testGeneratedSourceDirectory.toString(), this.basePackage.replace('.', '/')))

        createEasyExtensionManifest(extensionDirectory)
        createEasyTypeDescriptor(extensionDirectory)
        createInitScript(mainSourceDirectory)
        createEasyBeans(mainSourceDirectory)
        createImpExDirectories(extensionDirectory)
        createBackofficeDirectory(extensionDirectory)
        createEasyProperties(extensionDirectory)
        createReadMe(extensionDirectory)
        def tokenReplacements = [
                'EASY_GRADLE_PLUGIN_GROUP_ID': project.group.toString(),
                'EASY_GRADLE_PLUGIN_NAME'    : project.name,
                'EASY_GRADLE_PLUGIN_VERSION' : project.version.toString(),
                'EASY_API_BASE_URL'          : '',
                'EASY_API_KEY'               : '',
                'EASY_EXTENSION_ID'          : this.easyExtensionId,
                'EASY_VERSION'               : this.easyVersion,
                'EASY_EXTENSION_BASE_PACKAGE': this.basePackage,
                'EASY_REPOSITORY_CODE'       : ''
        ]
        copyResource("templates/easy-extension/my-extension.iml.vm", extensionDirectory, "${easyExtensionId}.iml", tokenReplacements)
        copyResource("templates/easy-extension/build.gradle.vm", extensionDirectory, "build.gradle", tokenReplacements)
        copyResource("templates/easy-extension/settings.gradle.vm", extensionDirectory, "settings.gradle", tokenReplacements)
        copyResource("templates/easy-extension/gradle.properties.vm", extensionDirectory, "gradle.properties", tokenReplacements)
    }

    private void createEasyExtensionManifest(File extensionDirectory) {
        logger.info("creating easy extension manifest 'easy.json' for extension '${this.easyExtensionId}'")
        File easyExtensionManifest = new File(extensionDirectory, "easy.json")
        this.createFile(easyExtensionManifest)

        EasyExtension extension = new EasyExtension()
        extension.groupId = this.basePackage
        extension.id = this.easyExtensionId
        extension.name = this.easyExtensionId
        extension.easyVersion = this.easyVersion

        def objectMapper = new ObjectMapper()
        easyExtensionManifest.text = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(extension)
        logger.info("created easy extension manifest 'easy.json' for extension '${this.easyExtensionId}'")
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

    private void createInitScript(Path extensionDirectory) {
        logger.info("creating 'Init.groovy' for extension '${this.easyExtensionId}'")
        this.createFile(new File(extensionDirectory.toFile(), "Init.groovy"))
        logger.info("created 'Init.groovy' for extension '${this.easyExtensionId}'")
    }

    private void createEasyBeans(Path extensionDirectory) {
        logger.info("creating 'EasyBeans.groovy' for extension '${this.easyExtensionId}'")
        this.createFile(new File(extensionDirectory.toFile(), "EasyBeans.groovy"))
        logger.info("created 'EasyBeans.groovy' for extension '${this.easyExtensionId}'")
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

    private void createImpExDirectories(File extensionDirectory) {
        logger.info("creating impex directories for extension '${this.easyExtensionId}'")
        Files.createDirectories(Path.of(extensionDirectory.toString(), 'impex', 'install'))
        Files.createDirectories(Path.of(extensionDirectory.toString(), 'impex', 'uninstall'))
        logger.info("created impex directories for extension '${this.easyExtensionId}'")
    }

    private void createBackofficeDirectory(File extensionDirectory) {
        logger.info("creating backoffice directory for extension '${this.easyExtensionId}'")
        Files.createDirectories(Path.of(extensionDirectory.toString(), 'backoffice'))
        logger.info("created backoffice directory for extension '${this.easyExtensionId}'")
    }

    private void createEasyProperties(File extensionDirectory) {
        logger.info("creating 'easy.properties' for extension '${this.easyExtensionId}'")
        this.createFile(new File(extensionDirectory, "easy.properties"))
        logger.info("created 'easy.properties' for extension '${this.easyExtensionId}'")
    }

    private void createReadMe(File extensionDirectory) {
        logger.info("creating 'README.md' for extension '${this.easyExtensionId}'")
        this.createFile(new File(extensionDirectory, "README.md"))
        logger.info("created 'README.md' for extension '${this.easyExtensionId}'")
    }

    private void copyResource(String template, File extensionDirectory, String target, Map<String, String> tokenReplacements) {
        logger.info("copying resource '${target}' from template '$template' for extension '${this.easyExtensionId}'")
        def resourceStream = Thread.currentThread().contextClassLoader.getResourceAsStream(template)
        if (null != resourceStream) {
            def velocityEngine = new VelocityEngine()
            velocityEngine.init()

            def contentTemplate = new Scanner(resourceStream).useDelimiter("\\A").next()
            def velocityContext = new VelocityContext()

            tokenReplacements.each {
                velocityContext.put(it.key, it.value)
            }

            def result = new StringWriter()
            velocityEngine.evaluate(velocityContext, result, 'replaceTokens', contentTemplate)

            def targetFile = Path.of(extensionDirectory.absolutePath, target).toFile()
            targetFile.text = result.toString()
            logger.info("copied resource '${target}' from template '$template' for extension '${this.easyExtensionId}'")
        } else {
            logger.info("Resource template '$template' not found")
        }
    }
}
