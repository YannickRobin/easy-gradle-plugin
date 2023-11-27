package com.sap.cx.boosters.easy.plugin.data

data class CommerceExtensionInfo(
    val name: String,
    val rootPath: String,
    val coreModule: Boolean,
    val webModule: Boolean,
    val requires: Set<String>
) {
    init {
        require(name.isNotBlank()) { "name is mandatory" }
        require(rootPath.isNotBlank()) { "rootPath is mandatory" }
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CommerceExtensionInfo) return false
        return this.name == other.name
    }
}
