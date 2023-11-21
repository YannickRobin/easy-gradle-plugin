package com.sap.cx.boosters.easy.gradleplugin.data

class EnumerationType {
    String code
    List<LocalizedString> name
    List<EnumerationValueType> values

    EnumerationType(){
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

    List<EnumerationValueType> getValues() {
        return values
    }

    void setValues(List<EnumerationValueType> values) {
        this.values = values
    }
}
