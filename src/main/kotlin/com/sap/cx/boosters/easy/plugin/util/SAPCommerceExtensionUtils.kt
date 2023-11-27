package com.sap.cx.boosters.easy.plugin.util

import com.sap.cx.boosters.easy.plugin.data.CommerceExtensionInfo
import groovy.text.GStringTemplateEngine
import groovy.xml.XmlSlurper
import org.apache.commons.lang3.StringUtils
import java.io.File
import kotlin.io.path.Path

object SAPCommerceExtensionUtils {

    fun buildPlatformClasspath(commercePlatformHome: String) : Set<File>{
        val classpath = mutableSetOf<File>()

        require(!StringUtils.isEmpty(commercePlatformHome))

        val commerceHomeDirectory = File(resolveHome(commercePlatformHome))

        val commercePlatformDirectory = File(commerceHomeDirectory, "bin/platform")

        require(!commercePlatformDirectory.exists())

        var localExtensionsFile = File(commerceHomeDirectory, "config/localextensions.xml")

        commercePlatformDirectory.listFiles{ file-> file.isFile && file.name.matches(Regex(".*\\.jar$")) }?.forEach { classpath.add(it) }

        commercePlatformDirectory.listFiles{ file-> file.isDirectory && file.name.matches(Regex("classes")) }?.forEach {
            if(!it.absolutePath.contains("eclipsebin")) classpath.add(it)
        }

        val extensions = getExtensions(localExtensionsFile)

    }

    private fun getExtensions(localExtensionsFile: File): Set<CommerceExtensionInfo> {
        val xmlParser = XmlSlurper()
        val templateEngine = GStringTemplateEngine()

        val commerceConfig = xmlParser.parse(localExtensionsFile)
        val commerceBinDirectory = Path(localExtensionsFile.parentFile.parent, "bin").toFile()
        val bindMap = mapOf("HYBRIS_BIN_DIR" to commerceBinDirectory.absolutePath)

        val extensions = mutableSetOf<CommerceExtensionInfo>()

        return extensions
    }

    private fun getExtensions(path: String, bindMap: Map<String, String>): Set<CommerceExtensionInfo> {
        val extensions = mutableSetOf<CommerceExtensionInfo>()
        val xmlParser = XmlSlurper()
        val templateEngine = GStringTemplateEngine()
        val extensionPath = File(templateEngine.createTemplate(path).make(bindMap).toString())
        traverseDirectory(extensionPath, "extensioninfo.xml", 3).forEach{
            val extensionInfo = xmlParser.parse(it)
            val extensionNode = extensionInfo.getProperty("extension")
        }
        return extensions
    }

    private fun traverseDirectory(directory:File, nameFilter: String, maxDepth: Int, currentDepth:Int=0): Set<File>{
        val result = mutableSetOf<File>()

        if (currentDepth <= maxDepth) {
            directory.listFiles()?.forEach { file ->
                if (file.isFile && file.name == nameFilter) {
                    result.add(file)
                } else if (file.isDirectory) {
                    result.addAll(traverseDirectory(file, nameFilter, maxDepth, currentDepth + 1))
                }
            }
        }

        return result
    }

    private fun resolveHome(path: String): String {
        return path.replace("~", System.getProperty("user.home"))
    }

}
