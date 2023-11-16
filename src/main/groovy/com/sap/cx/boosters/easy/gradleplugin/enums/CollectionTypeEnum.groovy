package com.sap.cx.boosters.easy.gradleplugin.enums

enum CollectionTypeEnum {
    SET("set"),
    LIST("list"),
    COLLECTION("collection")

    final String value

    CollectionTypeEnum(String value) {
        this.value = value
    }

    String getValue() {
        return value
    }
}
