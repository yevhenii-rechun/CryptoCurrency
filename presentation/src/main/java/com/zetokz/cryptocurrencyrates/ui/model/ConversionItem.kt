package com.zetokz.cryptocurrencyrates.ui.model

/**
 * Created by Yevhenii Rechun on 3/13/18.
 * Copyright © 2017. All rights reserved.
 */
data class ConversionItem(
    val baseCurrency: String,
    val conversionCurrencyName: String,
    val conversionCurrencyValue: Float
)