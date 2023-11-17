package com.sap.cx.boosters.easy.gradleplugin.plugin.extension

import org.gradle.api.provider.Property

interface EasyPluginExtension {
    Property<String> getBaseUrl()
    Property<String> getRepository()
    Property<String> getExtension()
    Property<String> getApiKey()
}
