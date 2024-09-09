package com.sap.cx.boosters.easy.gradleplugin.data

import com.sap.cx.boosters.easy.gradleplugin.enums.CardinalityTypeEnum
import com.sap.cx.boosters.easy.gradleplugin.enums.CollectionTypeEnum

class RelationElementType {
    String qualifier
    List<LocalizedString> name
    List<LocalizedString> description
    String type
    CardinalityTypeEnum cardinality
    CollectionTypeEnum collectiontype

    RelationElementType(){
        super()
    }

    String getQualifier() {
        return qualifier
    }

    void setQualifier(String qualifier) {
        this.qualifier = qualifier
    }

    List<LocalizedString> getName() {
        return name
    }

    void setName(List<LocalizedString> name) {
        this.name = name
    }

    List<LocalizedString> getDescription() {
        return description
    }

    void setDescription(List<LocalizedString> description) {
        this.description = description
    }

    String getType() {
        return type
    }

    void setType(String type) {
        this.type = type
    }

    CardinalityTypeEnum getCardinality() {
        return cardinality
    }

    void setCardinality(CardinalityTypeEnum cardinality) {
        this.cardinality = cardinality
    }

    CollectionTypeEnum getCollectiontype() {
        return collectiontype
    }

    void setCollectiontype(CollectionTypeEnum collectiontype) {
        this.collectiontype = collectiontype
    }
}
