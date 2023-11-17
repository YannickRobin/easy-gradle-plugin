package com.sap.cx.boosters.easy.gradleplugin

import com.sap.cx.boosters.easy.gradleplugin.data.CommerceExtensionInfo
import spock.lang.Specification

class EasyPluginTests extends Specification {

    def "compare extension info"() {

        when:
        def extA = new CommerceExtensionInfo(name: 'extA', rootPath: new File('a/b/c'))
        def extB = new CommerceExtensionInfo(name: 'extA', rootPath: new File('d/e/f'))

        then:
        extA == extB

    }

}
