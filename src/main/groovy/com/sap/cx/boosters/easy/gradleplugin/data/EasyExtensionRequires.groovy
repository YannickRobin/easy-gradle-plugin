package com.sap.cx.boosters.easy.gradleplugin.data

class EasyExtensionRequires {
    Set<String> commerce = new HashSet<>()
    Set<String> easy = new HashSet<>()

    Set<String> getCommerce() {
        return commerce
    }

    void setCommerce(Set<String> commerce) {
        this.commerce = commerce
    }

    Set<String> getEasy() {
        return easy
    }

    void setEasy(Set<String> easy) {
        this.easy = easy
    }
}
