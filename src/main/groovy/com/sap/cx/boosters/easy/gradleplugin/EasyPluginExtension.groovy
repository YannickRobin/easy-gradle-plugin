package com.sap.cx.boosters.easy.gradleplugin

import org.gradle.api.provider.Property

interface EasyPluginExtension {
    Property<String> getBaseUrl()
    Property<String> getRepository()
    Property<String> getExtension()
}