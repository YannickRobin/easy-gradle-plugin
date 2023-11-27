package com.sap.cx.boosters.easy.plugin.data

private const val CODE_IS_MANDATORY = "code is mandatory"

data class EasyTypes(
    val collectiontypes: List<CollectionType> = listOf(),
    val enumtypes: List<EnumerationType> = listOf(),
    val maptypes: List<MapType> = listOf(),
    val relations: List<RelationType> = listOf(),
    val itemtypes: List<ItemType> = listOf()
)

data class CollectionType(
    val code: String,
    val name: NameValue = NameValue("en", code),
    val elementType: String,
    val typeofCollection: CollectionTypeEnum = CollectionTypeEnum.COLLECTION
) {
    init {
        require(code.isNotBlank()) { CODE_IS_MANDATORY }
        require(elementType.isNotBlank()) { "elementType is mandatory" }
        require(
            CollectionTypeEnum.values().contains(typeofCollection)
        ) { "typeofCollection should be either of set, list or collection" }
    }
}

data class NameValue(
    val lang: String,
    val value: String
) {
    init {
        require(lang.isNotBlank()) { "lang is mandatory" }
        require(value.isNotBlank()) { "value is mandatory" }
    }
}

enum class CollectionTypeEnum(val value: String) {
    SET("set"),
    LIST("list"),
    COLLECTION("collection")
}

data class EnumerationType(
    val code: String,
    val name: NameValue = NameValue("en", code),
    val values: List<EnumerationValueType> = listOf()
) {
    init {
        require(code.isNotBlank()) { CODE_IS_MANDATORY }
    }
}

data class EnumerationValueType(
    val code: String,
    val name: NameValue = NameValue("en", code)
) {
    init {
        require(code.isNotBlank()) { CODE_IS_MANDATORY }
    }
}

data class MapType(
    val code: String,
    val name: NameValue = NameValue("en", code),
    val argumentType: String,
    val returnType: String
) {
    init {
        require(code.isNotBlank()) { CODE_IS_MANDATORY }
        require(argumentType.isNotBlank()) { "argumentType is mandatory" }
        require(returnType.isNotBlank()) { "returnType is mandatory" }
    }
}

data class RelationType(
    val code: String,
    val deployment: DeploymentType = DeploymentType(code, ""),
    val sourceElement: RelationElement,
    val targetElement: RelationElement,
) {
    init {
        require(code.isNotBlank()) { CODE_IS_MANDATORY }
        requireNotNull(sourceElement) { "sourceElement is mandatory" }
        requireNotNull(targetElement) { "sourceElement is mandatory" }
    }
}

data class RelationElement(
    val qualifier: String,
    val name: NameValue = NameValue("en", qualifier),
    val type: String,
    val cardinality: Cardinality,
    val collectiontype: CollectionTypeEnum?
) {
    init {
        require(qualifier.isNotBlank()) { "qualifier is mandatory" }
        require(type.isNotBlank()) { "type is mandatory" }
        require(Cardinality.values().contains(cardinality)) { "cardinality should be either of one or many" }
        require(
            CollectionTypeEnum.values().contains(collectiontype)
        ) { "collectiontype should be either of set, list or collection" }
    }
}

enum class Cardinality(code: String) {
    ONE("one"),
    MANY("many")
}

data class DeploymentType(
    val table: String,
    val typecode: String
) {
    init {
        require(table.isNotBlank()) { "table is mandatory" }
        require(typecode.isNotBlank()) { "typecode is mandatory" }
    }
}

data class ItemType(
    val code: String,
    val name: NameValue = NameValue("en", code),
    val autocreate: Boolean = true,
    val generate: Boolean = true,
    val superType: String = "GenericItem",
    val deployment: DeploymentType?,
    val attributes: List<AttributeType>?
) {
    init {
        require(code.isNotBlank()) { CODE_IS_MANDATORY }
    }
}

data class AttributeType(
    val qualifier: String,
    val name: NameValue = NameValue("en", qualifier),
    val type: String,
    val persistence: PersistenceType,
    val modifiers: AttributeModifiers?
) {
    init {
        require(qualifier.isNotBlank()) { "qualifier is mandatory" }
        require(type.isNotBlank()) { "type is mandatory" }
        requireNotNull(persistence) { "persistence is mandatory" }
    }

}

data class PersistenceType(
    val type: PersistenceTypeEnum,
    val column: String?,
    val attributeHandler: String?
) {
    init {
        require(
            PersistenceTypeEnum.values().contains(type)
        ) { "type is mandatory and either of 'property' or 'dynamic' " }
        if (type == PersistenceTypeEnum.DYNAMIC) {
            requireNotNull(attributeHandler) { "attributeHandler is mandatory for dynamic attribute" }
        }
    }
}

enum class PersistenceTypeEnum(type: String) {
    PROPERTY("property"),
    DYNAMIC("dynamic")
}

data class AttributeModifiers(
    val unique: Boolean = false,
    val initial: Boolean = false,
    val optional: Boolean = false,
    val write: Boolean = true,
    val partOf: Boolean = false
)
