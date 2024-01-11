package com.sap.cx.boosters.easy.gradleplugin

import com.sap.cx.boosters.easy.gradleplugin.data.CommerceExtensionInfo
import com.sap.cx.boosters.easy.gradleplugin.util.CommerceExtensionUtil
import groovy.io.FileType
import spock.lang.Specification

class EasyPluginTests extends Specification {

    def "compare extension info"() {

        when:
        def extA = new CommerceExtensionInfo(name: 'extA', rootPath: new File('a/b/c'))
        def extB = new CommerceExtensionInfo(name: 'extA', rootPath: new File('d/e/f'))

        then:
        extA == extB

    }

    def "dump classpath"() {

        when:
        def commerceHomeDirectory = '/Users/I309827/SAPDevelop/hybris/hybris-2211-easy/hybris'
        def coreExtDirectory = new File(commerceHomeDirectory, 'bin/platform/ext/core')

        // def filesFilter = /^.*ext\/.*\/lib\/.*\.jar$/
        // commercePlatformDirectory.traverse(type: FileType.FILES) {
        //     println "${it} [${it.absolutePath ==~ filesFilter}]"
        // }

        if (coreExtDirectory.exists()) {

            /*
            def classPath = CommerceExtensionUtil.buildPlatformClassPath(commerceHomeDirectory)
            classPath.each { k, v ->
                println k
                v.each {
                    println "  $it"
                }
            }
            */

            println 'test1'
            coreExtDirectory.traverse(type: FileType.FILES, nameFilter: ~/.*\.jar$/) {
                println it
            }

            println 'test2'
            def fileFilter = {File f -> f.canonicalPath ==~ /.*\/lib\/.*\.jar$/}
            coreExtDirectory.traverse(type: FileType.FILES, filter: fileFilter) {
                println it
            }

        }

        then:
        true

    }

}
