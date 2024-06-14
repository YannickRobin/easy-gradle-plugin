package com.sap.cx.boosters.easy.gradleplugin.util

import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

class ScaffoldingGenerator {

    private String templateFolder
    private Logger logger

    ScaffoldingGenerator(String templateFolder) {
        this.logger = Logging.getLogger(ScaffoldingGenerator)
        this.templateFolder = templateFolder
    }

    void copyTemplateFiles(List templateList, String destFolder, Map<String, String> parameters) {
        templateList.each { template ->
            copyTemplateFile(template as String, destFolder, parameters)
        }
    }

    void copyTemplateFile(String template, String destFolder, Map<String, String> parameters) {
        logger.info("copying resource '${template}'")

        InputStream templateStream = Thread.currentThread().contextClassLoader.getResourceAsStream("${templateFolder}/${template}")
        if (templateStream == null) {
            logger.error("Resource template '${template}' not found")
            return
        }

        if (isDirectory(template)) {
            logger.error("Resource template '${template}' is a directory and this is not supported")
            return
        }

        //Let's parse template for __MY_PARAMETER__ to replace by the parameter value
        parameters.each { key, value ->
            logger.info("Replacing ${key} with ${value}")
            template = template.replaceAll("__${key}__", value)
        }

        if (template.endsWith('.vm')) {
            template = template.substring(0, template.lastIndexOf('.vm'))
            Path targetFile = Paths.get(destFolder).resolve(template)
            copyVelocityFiles(templateStream, targetFile, parameters)
        } else {
            Path targetFile = Paths.get(destFolder).resolve(template)
            copyFiles(templateStream, targetFile)
        }

        logger.info("copied resource '${template}'")
    }

    private boolean isDirectory(String template) {
        String file = Thread.currentThread().contextClassLoader.getResource("${templateFolder}/${template}").getFile()
        int bangIndex = file.indexOf('!')
        String jarPath = file.substring(bangIndex + 2)
        file = new URL(file.substring(0, bangIndex)).getFile()
        ZipFile zip = new ZipFile(file)
        ZipEntry entry = zip.getEntry(jarPath)
        return entry.isDirectory()
    }

    private static void copyFiles(InputStream templateStream, Path targetPath) {
        Files.createDirectories(targetPath.getParent())
        targetPath.toFile().append(templateStream)
    }

    private static void copyVelocityFiles(InputStream templateStream, Path targetPath, Map<String, String> parameters) {
        def velocityEngine = new VelocityEngine()
        velocityEngine.init()

        def contentTemplate = new Scanner(templateStream).useDelimiter("\\A").next()
        def velocityContext = new VelocityContext()

        parameters.each {
            velocityContext.put(it.key, it.value)
        }

        def result = new StringWriter()
        velocityEngine.evaluate(velocityContext, result, 'replaceTokens', contentTemplate)

        Files.createDirectories(targetPath.getParent())
        def targetFile = targetPath.toFile()

        targetFile.text = result.toString()
    }

}
