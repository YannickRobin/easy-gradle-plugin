package com.sap.cx.boosters.easy.gradleplugin.tasks

import com.fasterxml.jackson.databind.ObjectMapper
import com.sap.cx.boosters.easy.gradleplugin.data.EasyExtension
import com.sap.cx.boosters.easy.gradleplugin.data.EasyTypes
import com.sap.cx.boosters.easy.gradleplugin.util.ClassGenerator
import org.apache.commons.lang3.StringUtils
import org.codehaus.groovy.GroovyException
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

import java.nio.file.Files
import java.nio.file.Path

class GenerateModelClassesTask extends AbstractEasyExtensionTask {

    @Internal
    private Properties easyProperties

    @Internal
    private EasyExtension easyExtension

    @Internal
    private EasyTypes easyTypes

    void init() {
        group = 'easy'
        description = 'generates model classes for easy types defined in easy extension'
    }

    @TaskAction
    void generate() {
        super.initializeExtensionId()
        this.validatePrerequisites()
        this.initializeProperties()
        logger.info("Generating models classes for extension: $extensionId")
        File basePackageDirectory = this.createBasePackageDirectory()
        if (null != easyTypes.enumtypes) {
            this.generateEnumerationClasses(basePackageDirectory)
        }

        if (null != easyTypes.itemtypes) {
            this.generateModelClasses(basePackageDirectory)
        }
        logger.info("Generated models classes for extension: $extensionId")
    }

    private void validatePrerequisites() {
        if (!project.projectDir.listFiles().any { it.name == "easy.json" }) {
            throw new GroovyException("The models classes can's be generated from outside of the easy extension directory.")
        }

        if (!project.projectDir.listFiles().any { it.name == "easytypes.json" }) {
            throw new GroovyException("The models classes can's be generated without easy type definitions in the easy extension.")
        }
    }

    private void initializeProperties() {
        this.easyProperties = new Properties()
        this.easyProperties.load(new FileInputStream(new File(project.projectDir, "easy.properties")))
        ObjectMapper mapper = new ObjectMapper()
        this.easyExtension = mapper.readValue(new File(project.projectDir, "easy.json"), EasyExtension.class)
        File easyTypeDefinitions = new File(project.projectDir, "easytypes.json")
        if (easyTypeDefinitions?.exists()) {
            this.easyTypes = mapper.readValue(new File(project.projectDir, "easytypes.json"), EasyTypes.class)
        }
    }

    private String getBasePackage() {
        String basePackage = this.getEasyProperties().getProperty("${this.getEasyExtension().id}.easy.type.base.models.package")
        if (StringUtils.isEmpty(basePackage)) {
            basePackage = "${this.getEasyExtension().groupId}.${this.getEasyExtension().id.replaceAll('[^a-zA-Z0-9]', '')}"
        }
        return basePackage
    }

    private File createBasePackageDirectory() {
        logger.info("Creating base package directory")

        Path modelsBaseDirectory = Path.of(project.projectDir.absolutePath, "gen${this.getEasyExtension().groovyMainPath}", this.getBasePackage().replaceAll('\\.', '/'))
        logger.info("Base package directory: $modelsBaseDirectory")
        if (modelsBaseDirectory.toFile().exists()) {
            modelsBaseDirectory.toFile().deleteDir()
        }
        Files.createDirectories(modelsBaseDirectory)
        logger.info("Created base package directory: $modelsBaseDirectory")

        logger.info("Created base package directory: $modelsBaseDirectory")
        return modelsBaseDirectory.toFile()
    }

    private void generateEnumerationClasses(File basePackageDirectory) {
        logger.info("Generating enumeration classes")
        if (basePackageDirectory.exists()) {
            File enumsDirectory = new File(basePackageDirectory, "enums")
            logger.info("Enums package directory: $enumsDirectory")
            if (!enumsDirectory.exists()) {
                Files.createDirectories(enumsDirectory.toPath())
                logger.info("Created enums package directory: $enumsDirectory")
            }
            String enumClassPackage = "${this.getBasePackage()}.enums"
            this.getEasyTypes().enumtypes.each {
                logger.info("Generating enumeration class for enum type: $it.code")
                if (enumsDirectory.exists()) {
                    File targetFile = Path.of(enumsDirectory.absolutePath, "${it.name}.groovy").toFile()
                    logger.info("Created enums class: $targetFile")
                    if (targetFile.exists()) {
                        targetFile.createNewFile()
                    }
                    targetFile.text = ClassGenerator.generateEnumClass(it, enumClassPackage)
                } else {
                    throw new GroovyException("enums package doesn't exist")
                }
                logger.info("Generated enumeration class for enum type: $it.code")
            }
        }
        logger.info("Generated enumeration classes")
    }

    private void generateModelClasses(File basePackageDirectory) {
        logger.info("Generating model classes")

        if (basePackageDirectory.exists()) {
            File modelsDirectory = new File(basePackageDirectory, "models")
            logger.info("Models package directory: $modelsDirectory")
            if (!modelsDirectory.exists()) {
                Files.createDirectories(modelsDirectory.toPath())
                logger.info("Created models package directory: $modelsDirectory")
            }
            String modelClassPackage = "${this.getBasePackage()}.models"
            this.getEasyTypes().itemtypes.each {
                logger.info("Generating model class for item type: $it.code")
                if (modelsDirectory.exists()) {
                    File targetFile = Path.of(modelsDirectory.absolutePath, "${it.modelClassName}.groovy").toFile()
                    logger.info("Created model class: $targetFile")
                    if (!targetFile.exists()) {
                        targetFile.createNewFile()
                    }
                    targetFile.text = ClassGenerator.generateModelClass(it, modelClassPackage)
                } else {
                    throw new GroovyException("models package doesn't exist")
                }
                logger.info("Generated model class for item type: $it.code")
            }
        }
        logger.info("Generated enumeration classes")
    }

    Properties getEasyProperties() {
        return easyProperties
    }

    EasyExtension getEasyExtension() {
        return easyExtension
    }

    EasyTypes getEasyTypes() {
        return easyTypes
    }

}
