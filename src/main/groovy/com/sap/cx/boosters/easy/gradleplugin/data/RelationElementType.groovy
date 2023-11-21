package com.sap.cx.boosters.easy.gradleplugin.data

import com.sap.cx.boosters.easy.gradleplugin.enums.CardinalityTypeEnum
import com.sap.cx.boosters.easy.gradleplugin.enums.CollectionTypeEnum

class RelationElementType {
    String qualifier
    Map<String, String> name
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

    Map<String, String> getName() {
        return name
    }

    void setName(Map<String, String> name) {
        this.name = name
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
