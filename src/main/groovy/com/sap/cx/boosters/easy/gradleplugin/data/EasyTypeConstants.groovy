package com.sap.cx.boosters.easy.gradleplugin.data

class EasyTypeConstants {
    def static final ATOMIC_TYPE_CLASS_MAPPING = [
            'java.lang.Object'                                   : 'java.lang.Object',
            'java.lang.Number'                                   : 'java.lang.Number',
            'java.lang.Integer'                                  : 'java.lang.Integer',
            'java.lang.Boolean'                                  : 'java.lang.Boolean',
            'java.lang.Byte'                                     : 'java.lang.Byte',
            'java.lang.Double'                                   : 'java.lang.Double',
            'java.lang.Float'                                    : 'java.lang.Float',
            'java.lang.Long'                                     : 'java.lang.Long',
            'java.lang.Short'                                    : 'java.lang.Short',
            'java.lang.String'                                   : 'java.lang.String',
            'java.lang.Character'                                : 'java.lang.Character',
            'java.util.Date'                                     : 'java.util.Date',
            'java.util.Map'                                      : 'java.util.Map',
            'java.lang.Class'                                    : 'java.lang.Class',
            'de.hybris.platform.util.ItemPropertyValue'          : 'de.hybris.platform.util.ItemPropertyValue',
            'de.hybris.platform.util.ItemPropertyValueCollection': 'de.hybris.platform.util.ItemPropertyValueCollection',
            'java.math.BigInteger'                               : 'java.math.BigInteger',
            'java.math.BigDecimal'                               : 'java.math.BigDecimal',
            'de.hybris.platform.core.PK'                         : 'de.hybris.platform.core.PK',
            'de.hybris.platform.util.TaxValue'                   : 'de.hybris.platform.util.TaxValue',
            'de.hybris.platform.util.DiscountValue'              : 'de.hybris.platform.util.DiscountValue',
            'de.hybris.platform.util.StandardDateRange'          : 'de.hybris.platform.util.StandardDateRange',
            'de.hybris.platform.core.order.EntryGroup'           : 'de.hybris.platform.core.order.EntryGroup'
    ]

    def static final EXCLUDED_IMPORTS = [
            'java.lang.Object',
            'java.lang.Number',
            'java.lang.Integer',
            'java.lang.Boolean',
            'java.lang.Byte',
            'java.lang.Double',
            'java.lang.Float',
            'java.lang.Long',
            'java.lang.Short',
            'java.lang.String',
            'java.lang.Character',
            'java.util.Locale',
            'java.util.List',
            'java.util.Set',
            'java.util.Map',
            'java.util.Date'
    ]
}
