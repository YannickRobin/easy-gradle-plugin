package com.sap.cx.boosters.easy.gradleplugin.data

class AttributeType {
    String qualifier
    List<LocalizedString> name
    String type
    String typeClass
    List<String> typeClassArguments
    Map<String, Boolean> modifiers
    PersistenceType persistence

    AttributeType() {
        super()
    }

    String getQualifier() {
        return qualifier
    }

    void setQualifier(String qualifier) {
        this.qualifier = qualifier
    }

    List<LocalizedString> getName() {
        return name
    }

    void setName(List<LocalizedString> name) {
        this.name = name
    }

    String getType() {
        return type
    }

    void setType(String type) {
        this.type = type
        def key = type
        if (key?.startsWith('localized:')) {
            key = key.split(':').last()
        }
        if (EasyTypeConstants.ATOMIC_TYPE_CLASS_MAPPING.containsKey(key)) {
            this.setTypeClass(EasyTypeConstants.ATOMIC_TYPE_CLASS_MAPPING[key])
        }
    }

    String getTypeClass() {
        return typeClass
    }

    void setTypeClass(String typeClass) {
        this.typeClass = typeClass
    }

    Map<String, Boolean> getModifiers() {
        return modifiers
    }

    void setModifiers(Map<String, Boolean> modifiers) {
        this.modifiers = modifiers
    }

    PersistenceType getPersistence() {
        return persistence
    }

    void setPersistence(PersistenceType persistence) {
        this.persistence = persistence
    }

    List<String> getTypeClassArguments() {
        return typeClassArguments
    }

    void setTypeClassArgument(List<String> typeClassArguments) {
        this.typeClassArguments = typeClassArguments
    }

}
