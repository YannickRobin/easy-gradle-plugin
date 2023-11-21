package com.sap.cx.boosters.easy.gradleplugin.enums

import com.fasterxml.jackson.annotation.JsonValue

enum PersistenceTypeEnum {
    PROPERTY("property"),
    DYNAMIC("dynamic")

    final String value

    PersistenceTypeEnum(String value){
        this.value = value
    }

    @JsonValue
    String getValue() {
        return value
    }
}
