package com.sap.cx.boosters.easy.gradleplugin.data

class MapType {
    String code
    Map<String, String> name
    String argumentType
    String returnType

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
