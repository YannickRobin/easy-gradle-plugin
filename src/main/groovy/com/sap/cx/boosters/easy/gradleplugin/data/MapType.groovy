package com.sap.cx.boosters.easy.gradleplugin.data

class MapType {
    String code
    List<LocalizedString> name
    String argumentType
    String returnType

    MapType(){
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

    String getArgumentType() {
        return argumentType
    }

    void setArgumentType(String argumentType) {
        this.argumentType = argumentType
    }

    String getReturnType() {
        return returnType
    }

    void setReturnType(String returnType) {
        this.returnType = returnType
    }
}
