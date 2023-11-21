package com.sap.cx.boosters.easy.gradleplugin.enums

import com.fasterxml.jackson.annotation.JsonValue

enum CollectionTypeEnum {
    SET("set"),
    LIST("list"),
    COLLECTION("collection")

    final String value

    CollectionTypeEnum(String value) {
        this.value = value
    }

    @JsonValue
    String getValue() {
        return value
    }
}
