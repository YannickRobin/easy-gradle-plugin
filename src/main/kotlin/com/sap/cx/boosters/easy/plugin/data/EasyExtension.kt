package com.sap.cx.boosters.easy.plugin.data

data class EasyExtension(
    val groupId: String,
    val id: String,
    val name: String,
    val version: String,
    val easyVersion: String,
    val description: String = "",
    val thumbnail: String = "",
    val documentation: String = "",
    val groovyMainPath: String = "",
    val groovyTestPath: String = "",
    val init: String = "",
    val authors: List<String> = listOf(),
    val requires: Requires = Requires(),
    val tags: List<String> = listOf(),
    val vendor: String = "",

    ) {
    init {
        require(groupId.isNotBlank()) { "groupId is mandatory" }
        require(id.isNotBlank()) { "id is mandatory" }
        require(name.isNotBlank()) { "name is mandatory" }
        require(version.isNotBlank()) { "version is mandatory" }
        require(easyVersion.isNotBlank()) { "easyVersion is mandatory" }
    }
}

data class Requires(
    val commerce: List<String> = listOf(),
    val easy: List<String> = listOf()
)
