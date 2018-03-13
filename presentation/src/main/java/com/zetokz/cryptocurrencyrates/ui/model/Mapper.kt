package com.zetokz.cryptocurrencyrates.ui.model

import com.zetokz.data.RestConfig
import com.zetokz.data.model.Currency

/**
 * Created by Yevhenii Rechun on 3/10/18.
 * Copyright © 2017. All rights reserved.
 */

fun Currency.toCurrencyItem() = CurrencyItem(id.toLong(), RestConfig.IMAGE_BASE_URL + imageUrl, name, fullName)

fun List<Currency>.toCurrencyItems() = asSequence().map(Currency::toCurrencyItem).toList()

fun Currency.toCurrencySelectableItem() =
    CurrencyItemSelectable(RestConfig.IMAGE_BASE_URL + imageUrl, name, fullName, false)

fun List<Currency>.toCurrencySelectableItems() = asSequence().map(Currency::toCurrencySelectableItem).toList()