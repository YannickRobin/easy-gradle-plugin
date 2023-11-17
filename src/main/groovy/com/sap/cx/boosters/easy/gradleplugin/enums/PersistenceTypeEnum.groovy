package com.sap.cx.boosters.easy.gradleplugin.enums

enum PersistenceTypeEnum {
    PROPERTY("property"),
    DYNAMIC("dynamic")

    final String value

    PersistenceTypeEnum(String value){
        this.value = value
    }

    String getValue() {
        return value
    }
}
