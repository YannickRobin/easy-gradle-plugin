package com.sap.cx.boosters.easy.gradleplugin.data

import com.sap.cx.boosters.easy.gradleplugin.enums.CollectionTypeEnum

class CollectionType {
    String code
    Map<String, String> name
    String elementType
    CollectionTypeEnum typeOfCollection = CollectionTypeEnum.COLLECTION

    CollectionType(String code, Map<String, String> name, String elementType, CollectionTypeEnum typeOfCollection) {
        this.code = code
        this.name = name
        this.elementType = elementType
        this.typeOfCollection = typeOfCollection
    }

    String getCode() {
        return code
    }

    void setCode(String code) {
        this.code = code
    }

    Map<String, String> getName() {
        return name
    }

    void setName(Map<String, String> name) {
        this.name = name
    }

    String getElementType() {
        return elementType
    }

    void setElementType(String elementType) {
        this.elementType = elementType
    }

    CollectionTypeEnum getTypeOfCollection() {
        return typeOfCollection
    }

    void setTypeOfCollection(CollectionTypeEnum typeOfCollection) {
        this.typeOfCollection = typeOfCollection
    }

}
