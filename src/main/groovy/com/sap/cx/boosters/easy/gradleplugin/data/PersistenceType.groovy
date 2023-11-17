package com.sap.cx.boosters.easy.gradleplugin.data

import com.sap.cx.boosters.easy.gradleplugin.enums.PersistenceTypeEnum

class PersistenceType {
    PersistenceTypeEnum type
    String column
    String attributeHandler

    PersistenceType(PersistenceTypeEnum type) {
        this.type = type
    }

    PersistenceType(PersistenceTypeEnum type, String attributeHandler) {
        this.type = type
        this.attributeHandler = attributeHandler
    }

    PersistenceTypeEnum getType() {
        return type
    }

    void setType(PersistenceTypeEnum type) {
        this.type = type
    }

    String getColumn() {
        return column
    }

    void setColumn(String column) {
        this.column = column
    }

    String getAttributeHandler() {
        return attributeHandler
    }

    void setAttributeHandler(String attributeHandler) {
        this.attributeHandler = attributeHandler
    }
}
