package com.zetokz.cryptocurrencyrates.ui.model

import com.zetokz.data.model.Currency

/**
 * Created by Yevhenii Rechun on 3/10/18.
 * Copyright © 2017. All rights reserved.
 */

fun Currency.toCurrencyItem() = CurrencyItem(imageUrl, name, fullName)

fun List<Currency>.toCurrencyItems() = asSequence().map { it.toCurrencyItem() }.toList()