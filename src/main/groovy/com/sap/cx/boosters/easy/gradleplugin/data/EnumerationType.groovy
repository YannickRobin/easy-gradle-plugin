package com.sap.cx.boosters.easy.gradleplugin.data

class EnumerationType {
    String code
    Map<String, String> name
    List<EnumerationValueType> values

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

    List<EnumerationValueType> getValues() {
        return values
    }

    void setValues(List<EnumerationValueType> values) {
        this.values = values
    }
}
