package com.sap.cx.boosters.easy.plugin.tasks

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.sap.cx.boosters.easy.plugin.data.EasyExtension
import com.sap.cx.boosters.easy.plugin.data.EasyTypes
import com.sap.cx.boosters.easy.plugin.data.EnumerationType
import com.sap.cx.boosters.easy.plugin.data.ItemType
import groovy.text.SimpleTemplateEngine
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.util.*

class GenerateModelClassesTask : AbstractEasyTask() {
    init {
        description =
            "Generates model classes based on the definitions defined in 'easytypes.json' in an easy extension"
    }

    @TaskAction
    fun generate() {
        val easyProperties = Properties()
        File("easy.properties").inputStream().use {
            easyProperties.load(it)
        }

        val easyJSONFile = File(project.projectDir, "easy.json")

        val easyExtensionId = jacksonObjectMapper().readValue<EasyExtension>(easyJSONFile).id

        val baseModelClassesPackageKey = "easyextension.$easyExtensionId.easy.type.base.models.package"

        val baseModelClassesPackage = easyProperties.getProperty(baseModelClassesPackageKey)

        val modelClassesDirectory = createBaseModelClassPackageDirectory(baseModelClassesPackage)

        val easyTypeJSONFile = File(project.projectDir, "easytypes.json")
        if(easyTypeJSONFile.exists()){
            val easyTypes = jacksonObjectMapper().readValue<EasyTypes>(easyJSONFile)

            val enumTypes = easyTypes.enumtypes
            createEnumTypeClasses(modelClassesDirectory, enumTypes)
            val itemTypes = easyTypes.itemtypes
            createModelClasses(modelClassesDirectory, itemTypes)
        }

    }

    private fun createBaseModelClassPackageDirectory(baseModelClassesPackage: String?): File? {
        val baseModelClassesPackagePath = baseModelClassesPackage?.replace('.', '/')
        val baseModelClassesPackageDirectory = baseModelClassesPackagePath?.let { File(project.projectDir, it) }
        if (baseModelClassesPackageDirectory != null && !baseModelClassesPackageDirectory.exists()) {
            baseModelClassesPackageDirectory.mkdirs();
        }
        return baseModelClassesPackageDirectory
    }

    private fun createEnumTypeClasses(modelClassesDirectory: File?, enumTypes: List<EnumerationType>){
        if (modelClassesDirectory != null && modelClassesDirectory.exists()){
            val enumDirectory = File(modelClassesDirectory, "enum")
            if(!enumDirectory.exists()){
                enumDirectory.mkdirs()
            }
            enumTypes.forEach {
                createEnumTypeClass(enumDirectory, it)
            }
        }
    }

    private fun createEnumTypeClass(packageDirectory: File, enumTypeInstance: EnumerationType){
        val enumGroovyClassFile = File(packageDirectory, enumTypeInstance.code)
        if(!enumGroovyClassFile.exists()){
            enumGroovyClassFile.createNewFile()
        }

        val binding = mapOf("enumType" to enumTypeInstance.code, "enumValues" to enumTypeInstance.values.map { it.code })

        val templateFileStream = Thread.currentThread().contextClassLoader.getResourceAsStream("enumtemplate.template")
        if(null!=templateFileStream) {
            val template = SimpleTemplateEngine().createTemplate(templateFileStream.reader())

            val generatedClassContent = template.make(binding)
            enumGroovyClassFile.writeText(generatedClassContent.toString())
        }
    }

    private fun createModelClasses(modelClassesDirectory: File?, itemTypes: List<ItemType>){
        if (modelClassesDirectory != null && modelClassesDirectory.exists()){
            val modelDirectory = File(modelClassesDirectory, "model")
            if(!modelDirectory.exists()){
                modelDirectory.mkdirs()
            }
            itemTypes.forEach {
                createModelClass(modelDirectory, it)
            }
        }
    }

    private fun createModelClass(packageDirectory: File, itemType: ItemType){
        val modelGroovyClassFile = File(packageDirectory, itemType.code)
        if(!modelGroovyClassFile.exists()){
            modelGroovyClassFile.createNewFile()
        }


    }

}
