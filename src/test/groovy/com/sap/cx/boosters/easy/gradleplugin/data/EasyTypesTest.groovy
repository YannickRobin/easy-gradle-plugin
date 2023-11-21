package com.sap.cx.boosters.easy.gradleplugin.data

import com.fasterxml.jackson.databind.ObjectMapper
import com.sap.cx.boosters.easy.gradleplugin.util.ClassGenerator
import spock.lang.Specification

class EasyTypesTest extends Specification {

    def "parse easy types"() {
        given:
        def easyTypesDefFile = "easytypes.json"

        when:
        def easyTypes = new ObjectMapper().readValue(new File(getClass().classLoader.getResource(easyTypesDefFile).file), EasyTypes.class)

        then:
        easyTypes.itemtypes[0].code == 'TestItem'
    }

    def "generate enum"() {
        given:
        def enumType = new EnumerationType()
        enumType.code = 'TestEnum'
        def enumValue = new EnumerationValueType()
        enumValue.code = 'Test'
        enumType.values = [enumValue]
        def packageName = "com.sap.cx.booster.easy.enums"
        when:
        def result = ClassGenerator.generateEnumClass(enumType, packageName)

        then:
        result != null
        println(result)
    }

    def "generate model"() {
        given:
        def customProduct = new ItemType()
        customProduct.code = 'Product'
        customProduct.modelClassName = 'CustomProductModel'
        customProduct.superTypeModelClass = 'de.hybris.platform.core.model.product.ProductModel'

        def code = new AttributeType()
        code.qualifier = 'code'
        code.type = 'java.lang.String'
        code.typeClass = 'java.lang.String'

        def name = new AttributeType()
        name.qualifier = 'name'
        name.type = 'localized:java.lang.String'
        name.typeClass = 'java.lang.String'

        def country = new AttributeType()
        country.qualifier = 'country'
        country.type = 'Country'
        country.typeClass = 'de.hybris.platform.core.model.c2l.CountryModel'

        customProduct.attributes = [code, name, country]

        def packageName = "com.sap.cx.booster.easy.models"
        when:
        def result = ClassGenerator.generateModelClass(customProduct, packageName)

        then:
        result != null
        println(result)
    }
}
