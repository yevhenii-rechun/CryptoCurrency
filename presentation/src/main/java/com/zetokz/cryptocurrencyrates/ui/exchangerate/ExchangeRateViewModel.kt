package com.zetokz.cryptocurrencyrates.ui.exchangerate

import com.zetokz.cryptocurrencyrates.base.BaseViewModel
import com.zetokz.cryptocurrencyrates.ui.addcurrency.AddCurrencyRouter
import com.zetokz.data.interactor.CurrencyRatesInteractor
import javax.inject.Inject

/**
 * Created by Yevhenii Rechun on 3/12/18.
 * Copyright © 2017. All rights reserved.
 */
class ExchangeRateViewModel @Inject constructor(
    addCurrencyRouter: AddCurrencyRouter,
    currencyRatesInteractor: CurrencyRatesInteractor
) : BaseViewModel()