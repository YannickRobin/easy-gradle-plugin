package com.sap.cx.boosters.easy.gradleplugin

import org.slf4j.LoggerFactory
import spock.lang.Specification

class EasyPluginTests extends Specification {

    def "get required extensions"() {

        when:
        def localExtensionsFile = '~/SAPDevelop/hybris/hybris-2211-easy/hybris/config/localextensions.xml'
        def commerceExtensionHelper = new CommerceExtensionHelper(LoggerFactory.getLogger('spock-tests'))
        def extensions = commerceExtensionHelper.getExtensions(new File(commerceExtensionHelper.resolveHome(localExtensionsFile)))
        extensions.each {println it.name}
        println "extension founds: ${extensions.size()}"

        then:
        !extensions.isEmpty()

    }

    def "compare extension info"() {

        when:
        def extA = new CommerceExtensionInfo(name: 'extA', rootPath: new File('a/b/c'))
        def extB = new CommerceExtensionInfo(name: 'extA', rootPath: new File('d/e/f'))

        then: extA == extB

    }

}
