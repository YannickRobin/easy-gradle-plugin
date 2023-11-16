package com.sap.cx.boosters.easy.gradleplugin.data

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includes = ['name'])
class CommerceExtensionInfo {

    String name
    File rootPath
    Boolean coremodule, webmodule
    Set<String> requires

}
