package com.sap.cx.boosters.easy.gradleplugin.enums

import com.fasterxml.jackson.annotation.JsonValue

enum CardinalityTypeEnum {
    ONE("one"),
    MANY("many")

    final String value

    CardinalityTypeEnum(String value){
        this.value = value
    }

    @JsonValue
    String getValue() {
        return value
    }
}
