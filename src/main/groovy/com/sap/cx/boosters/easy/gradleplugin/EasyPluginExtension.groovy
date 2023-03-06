package com.sap.cx.boosters.easyplugin

import org.gradle.api.provider.Property

interface EasyPluginExtension {
    Property<String> getBaseUrl()
    Property<String> getRepository()
    Property<String> getExtension()
}