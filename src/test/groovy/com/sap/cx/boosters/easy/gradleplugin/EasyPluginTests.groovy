package com.sap.cx.boosters.easy.gradleplugin

import spock.lang.Specification

class EasyPluginTests extends Specification {

    def "get required extensions"() {

        when:
        def localExtensionsFile = '~/SAPDevelop/hybris/hybris-2211-easy/hybris/config/localextensions.xml'
        def extensions = EasyPluginUtil.getExtensions(new File(EasyPluginUtil.resolveHome(localExtensionsFile)))
        extensions.each {println it.name}
        println "extension founds: ${extensions.size()}"

        then:
        !extensions.isEmpty()

    }

}
