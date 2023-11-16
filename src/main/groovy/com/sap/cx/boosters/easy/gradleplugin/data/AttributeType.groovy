package com.sap.cx.boosters.easy.gradleplugin.data

class AttributeType {
    String qualifier
    Map<String, String> name
    String type
    Map<String, Boolean> modifiers

    String getQualifier() {
        return qualifier
    }

    void setQualifier(String qualifier) {
        this.qualifier = qualifier
    }

    Map<String, String> getName() {
        return name
    }

    void setName(Map<String, String> name) {
        this.name = name
    }

    String getType() {
        return type
    }

    void setType(String type) {
        this.type = type
    }

    Map<String, Boolean> getModifiers() {
        return modifiers
    }

    void setModifiers(Map<String, Boolean> modifiers) {
        this.modifiers = modifiers
    }
}
