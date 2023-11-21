package com.sap.cx.boosters.easy.gradleplugin.data

class EnumerationValueType {
    String code
    List<LocalizedString> name

    EnumerationValueType(){
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
}
