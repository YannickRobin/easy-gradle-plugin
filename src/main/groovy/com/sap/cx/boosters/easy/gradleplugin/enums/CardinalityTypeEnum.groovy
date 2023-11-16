package com.sap.cx.boosters.easy.gradleplugin.enums

enum CardinalityTypeEnum {
    ONE("one"),
    MANY("many")

    final String value

    CardinalityTypeEnum(String value){
        this.value = value
    }

    String getValue() {
        return value
    }
}
