package com.sap.cx.boosters.easy.gradleplugin.data

class DeploymentType {
    String table
    String typecode

    String getTable() {
        return table
    }

    void setTable(String table) {
        this.table = table
    }

    String getTypecode() {
        return typecode
    }

    void setTypecode(String typecode) {
        this.typecode = typecode
    }
}
