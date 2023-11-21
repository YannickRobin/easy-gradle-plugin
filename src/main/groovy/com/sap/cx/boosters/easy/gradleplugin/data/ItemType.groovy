package com.sap.cx.boosters.easy.gradleplugin.data

class ItemType {
    String code
    List<LocalizedString> name
    boolean autocreate = true
    boolean generate = true
    String superType = 'GenericItem'
    DeploymentType deployment
    Set<AttributeType> attributes
    String modelClassName
    String superTypeModelClass = 'de.hybris.platform.core.model.ItemModel'

    ItemType(){
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

    boolean getAutocreate() {
        return autocreate
    }

    void setAutocreate(boolean autocreate) {
        this.autocreate = autocreate
    }

    boolean getGenerate() {
        return generate
    }

    void setGenerate(boolean generate) {
        this.generate = generate
    }

    String getSuperType() {
        return superType
    }

    void setSuperType(String superType) {
        this.superType = superType
    }

    DeploymentType getDeployment() {
        return deployment
    }

    void setDeployment(DeploymentType deployment) {
        this.deployment = deployment
    }


    Set<AttributeType> getAttributes() {
        return attributes
    }

    void setAttributes(Set<AttributeType> attributes) {
        this.attributes = attributes
    }

    String getModelClassName() {
        return modelClassName
    }

    void setModelClassName(String modelClassName) {
        this.modelClassName = modelClassName
    }

    String getSuperTypeModelClass() {
        return superTypeModelClass
    }

    void setSuperTypeModelClass(String superTypeModelClass) {
        this.superTypeModelClass = superTypeModelClass
    }
}
