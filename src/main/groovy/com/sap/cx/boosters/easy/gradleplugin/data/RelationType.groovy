package com.sap.cx.boosters.easy.gradleplugin.data

class RelationType {
    String code
    DeploymentType deployment
    RelationElementType sourceElement
    RelationElementType targetElement

    String getCode() {
        return code
    }

    void setCode(String code) {
        this.code = code
    }

    DeploymentType getDeployment() {
        return deployment
    }

    void setDeployment(DeploymentType deployment) {
        this.deployment = deployment
    }

    RelationElementType getSourceElement() {
        return sourceElement
    }

    void setSourceElement(RelationElementType sourceElement) {
        this.sourceElement = sourceElement
    }

    RelationElementType getTargetElement() {
        return targetElement
    }

    void setTargetElement(RelationElementType targetElement) {
        this.targetElement = targetElement
    }
}
