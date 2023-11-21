package com.sap.cx.boosters.easy.gradleplugin.data

import com.sap.cx.boosters.easy.gradleplugin.enums.CollectionTypeEnum

class CollectionType {
    String code
    List<LocalizedString> name
    String elementType
    CollectionTypeEnum typeOfCollection = CollectionTypeEnum.COLLECTION

    CollectionType(){
        super()
    }

    String getCode() {
        return code
    }

    void setCode(String code) {
        this.code = code
    }

    List<LocalizedString> getName() {
        return name
    }

    void setName(List<LocalizedString> name) {
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
