package com.sap.cx.boosters.easy.gradleplugin.data

class EnumerationValueType {
    String code
    Map<String, String> name

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
}
