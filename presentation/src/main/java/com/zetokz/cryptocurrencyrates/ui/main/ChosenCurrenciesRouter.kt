package com.zetokz.cryptocurrencyrates.ui.main

import com.zetokz.cryptocurrencyrates.ui.model.CurrencyItem

/**
 * Created by Yevhenii Rechun on 3/10/18.
 * Copyright © 2017. All rights reserved.
 */
interface ChosenCurrenciesRouter {

    fun navigateToAddCurrency()

    fun navigateToCurrencyExchangeRates(currencyItem: CurrencyItem)
}