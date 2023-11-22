package com.sap.cx.boosters.easy.gradleplugin.util

import com.sap.cx.boosters.easy.gradleplugin.data.EasyTypeConstants
import com.sap.cx.boosters.easy.gradleplugin.data.EnumerationType
import com.sap.cx.boosters.easy.gradleplugin.data.ItemType
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine

class ClassGenerator {

    static final String ENUMERATION_TEMPLATE = "templates/enums/enum-class.vm"

    static final String MODEL_TEMPLATE = "templates/models/model-class.vm"

    static def generateEnumClass(EnumerationType enumerationType, String packageName){
        def velocityContext = createVelocityContext()
        velocityContext.put("package", packageName)
        velocityContext.put("enumType", enumerationType)
        return generateClass(velocityContext, ENUMERATION_TEMPLATE)
    }

    static def generateModelClass(ItemType itemType, String packageName){
        def velocityContext = createVelocityContext()
        velocityContext.put("package", packageName)
        velocityContext.put("itemType", itemType)
        return generateClass(velocityContext, MODEL_TEMPLATE)
    }

    private static def createVelocityContext(){
        def velocityContext = new VelocityContext()
        velocityContext.put("currentDate", new Date())
        velocityContext.put("year", Calendar.getInstance().get(Calendar.YEAR))
        velocityContext.put("excludedImports", EasyTypeConstants.EXCLUDED_IMPORTS)
        return velocityContext
    }

    private static String generateClass(VelocityContext velocityContext, def velocityTemplateFileName) {
        def velocityEngine = new VelocityEngine()
        velocityEngine.init()
        def template = new Scanner(Thread.currentThread().contextClassLoader.getResourceAsStream(velocityTemplateFileName)).useDelimiter("\\A").next()

        def result = new StringWriter()
        velocityEngine.evaluate(velocityContext, result, 'Generated Easy Extension Framework Class Template', template)

        return result.toString()
    }

}
