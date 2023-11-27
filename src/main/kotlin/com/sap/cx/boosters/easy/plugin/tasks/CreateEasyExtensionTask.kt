package com.sap.cx.boosters.easy.plugin.tasks

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.sap.cx.boosters.easy.plugin.data.EasyExtension
import org.apache.commons.lang3.StringUtils
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

abstract class CreateEasyExtensionTask() : DefaultTask() {

    @Input
    var extensionId: String = ""

    @Input
    var easyVersion: String = ""

    @Input
    var basePackage: String = ""

    init {
        group = "easy"
        description = "Creates a new Easy Extension in current directory"
    }

    @TaskAction
    fun createExtension() {
        logger.info("Starting to a new easy extension in current directory")
        setEasyExtensionId()
        setEasyVersion()
        setBasePackage()

        createExtensionInternal()
        updateGradleResources()
        println("Easy extension $extensionId created.")
    }

    private fun updateGradleResources(){
        addPropertyToFile(Path.of(project.projectDir.absolutePath, extensionId, "gradle.properties"), "sap.commerce.easy.extension.id", extensionId)
        addPropertyToFile(Path.of(project.projectDir.absolutePath, extensionId, "settings.gradle.kts"), "rootProject.name", "\"$extensionId\"")
    }

    private fun addPropertyToFile(path: Path, key: String, value: String){
        val file = path.toFile()
        val existingContent = if(file.exists()) file.readText() else ""
        val updatedContent = "$existingContent\n$key=$value"
        Files.write(path, updatedContent.toByteArray(), StandardOpenOption.CREATE, StandardOpenOption.WRITE)
    }

    private fun createExtensionInternal() {

        val extensionDirectory = File(project.projectDir, extensionId)
        if (extensionDirectory.exists()) {
            logger.info("Easy extension already exists")
        } else {
            if (extensionDirectory.mkdirs()) {
                logger.info("Extension directory created: $extensionDirectory")
            } else {
                throw GradleException("Failed to create extension directory: $extensionDirectory")
            }
        }

        logger.info(extensionDirectory.absolutePath)
        val mainSrcDirectory = createSubDirectories(extensionDirectory.absolutePath, "src/main/groovy")
        val testSrcDirectory = createSubDirectories(extensionDirectory.absolutePath, "src/test/groovy")
        val packagePath = basePackage.replace(".", "/")
        createSubDirectories(mainSrcDirectory, packagePath)
        createSubDirectories(testSrcDirectory, packagePath)

        createManifest(extensionDirectory.absolutePath)
        createInitScript(mainSrcDirectory)
        createEasyBeans(mainSrcDirectory)
        createImpExDirectories(extensionDirectory.absolutePath)
        createBackofficeDirectory(extensionDirectory.absolutePath)
        createEasyProperties(extensionDirectory)
        createReadMe(extensionDirectory)

        copyResource("my-extension.iml", "$extensionId.iml")
        copyResource("build.gradle.kts.template", "build.gradle.kts")
        copyResource("settings.gradle.kts.template", "settings.gradle.kts")
        copyResource("gradle.properties.template", "gradle.properties")

    }

    private fun copyResource(resourceName: String, targetName: String) {
        val resourceStream = Thread.currentThread().contextClassLoader.getResourceAsStream(resourceName)
        if (null != resourceStream) {
            Files.copy(resourceStream, Path.of(project.projectDir.absolutePath, extensionId, targetName))
        }
    }

    private fun createImpExDirectories(baseDirectory: String) {
        createSubDirectories(baseDirectory, "impex/install")
        createSubDirectories(baseDirectory, "impex/uninstall")
    }

    private fun createBackofficeDirectory(baseDirectory: String) {
        createSubDirectories(baseDirectory, "backoffice")
    }

    private fun createManifest(baseDirectory: String) {
        val manifestFile = File(baseDirectory, "easy.json")
        if (!manifestFile.exists() && manifestFile.createNewFile()) {
            logger.info("created extension manifest: $manifestFile")
        } else {
            throw GradleException("could not create extension manifest: $manifestFile")
        }

        val extension = EasyExtension(
            basePackage,
            extensionId,
            extensionId,
            "0.0.1",
            easyVersion
        )
        val objectMapper = jacksonObjectMapper()
        val json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(extension)

        manifestFile.writeText(json)
    }

    private fun createInitScript(baseDirectory: String) {
        val initScriptFile = File(baseDirectory, "Init.groovy")
        if (!initScriptFile.exists() && initScriptFile.createNewFile()) {
            logger.info("created init script: $initScriptFile")
        } else {
            throw GradleException("could not create init script: $initScriptFile")
        }
    }

    private fun createReadMe(baseDirectory: File) {
        val readMeFile = File(baseDirectory, "README.md")
        if (!readMeFile.exists() && readMeFile.createNewFile()) {
            logger.info("created documentation file: $readMeFile")
        } else {
            throw GradleException("could not create documentation file: $readMeFile")
        }
    }

    private fun createEasyProperties(baseDirectory: File) {
        val easyProperties = File(baseDirectory, "easy.properties")
        if (!easyProperties.exists() && easyProperties.createNewFile()) {
            logger.info("created properties file: $easyProperties")
        } else {
            throw GradleException("could not create properties file: $easyProperties")
        }
    }

    private fun createEasyBeans(baseDirectory: String) {
        val easyBeans = File(baseDirectory, "EasyBeans.groovy")
        if (!easyBeans.exists() && easyBeans.createNewFile()) {
            logger.info("created easy beans file: $easyBeans")
        } else {
            throw GradleException("could not create easy beans file: $easyBeans")
        }
    }

    private fun createSubDirectories(sourceDirectory: String, subDirectoriesPath: String): String {

        val subDirectory = Files.createDirectories(Path.of(sourceDirectory, subDirectoriesPath))
        return subDirectory.toString()
    }

    private fun setEasyExtensionId() {
        if (extensionId.isBlank()) {
            val userInput = project.gradle.startParameter.projectProperties["extensionId"]
            extensionId = if (!userInput.isNullOrBlank()) {
                userInput
            } else {
                logger.info("Enter the easy extension id [my-extension]: ")
                val input = readlnOrNull().toString()
                input.ifBlank {
                    "my-extension"
                }
            }
        }
    }

    private fun setEasyVersion() {
        if (StringUtils.isEmpty(easyVersion)) {
            easyVersion = if (project.properties.contains("sap.commerce.easy.api.key")) {
                project.property("sap.commerce.easy.extension.easy.version") as String
            } else {
                val userInput = project.gradle.startParameter.projectProperties["easyVersion"]
                if (!userInput.isNullOrBlank()) {
                    userInput
                } else {
                    logger.info("Enter the easy version [0.2]: ")
                    val input = readlnOrNull().toString()
                    input.ifBlank {
                        "0.2"
                    }
                }
            }
        }
    }

    private fun setBasePackage() {
        if (StringUtils.isEmpty(basePackage)) {
            basePackage = if (project.properties.contains("sap.commerce.easy.extension.base.package")) {
                project.property("sap.commerce.easy.extension.base.package") as String
            } else {
                val userInput = project.gradle.startParameter.projectProperties["basePackage"]
                if (!userInput.isNullOrBlank()) {
                    userInput
                } else {
                    logger.info("Enter the base package [com.sap.cx.boosters.easy]: ")
                    val input = readlnOrNull().toString()
                    input.ifBlank {
                        "com.sap.cx.boosters.easy"
                    }
                }
            }
        }
    }
}
