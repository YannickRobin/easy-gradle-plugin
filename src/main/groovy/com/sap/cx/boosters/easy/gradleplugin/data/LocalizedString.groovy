package com.sap.cx.boosters.easy.gradleplugin.data

class LocalizedString {
    String lang
    String value

    LocalizedString(){
        super()
    }

    String getLang() {
        return lang
    }

    void setLang(String lang) {
        this.lang = lang
    }

    String getValue() {
        return value
    }

    void setValue(String value) {
        this.value = value
    }
}
