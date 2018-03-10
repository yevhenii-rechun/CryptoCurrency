package com.zetokz.data.model

/**
 * Created by Yevhenii Rechun on 3/10/18.
 * Copyright © 2017. All rights reserved.
 */
class ConversionResponse : HashMap<String, Float>()

internal fun ConversionResponse.toConversionList(baseCurrencyName: String): List<Conversion> =
    iterator().asSequence().map { Conversion(baseCurrencyName, it.key, it.value) }.toList()