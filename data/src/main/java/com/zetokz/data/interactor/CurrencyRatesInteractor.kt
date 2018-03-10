package com.zetokz.data.interactor

import com.zetokz.data.model.Conversion
import com.zetokz.data.model.Currency
import com.zetokz.data.repository.CurrencyRatesRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by Yevhenii Rechun on 1/16/18.
 * Copyright © 2017. All rights reserved.
 */
class CurrencyRatesInteractor @Inject constructor(
    private val currencyRatesRepository: CurrencyRatesRepository
) {

    fun getChosenCurrencies(): Single<List<Currency>> =
        currencyRatesRepository.getChosenCurrencies()

    fun getAvailableCurrencies(): Single<List<Currency>> =
        currencyRatesRepository.getAvailableCurrencies()

    fun getExchangeRates(baseCurrency: String): List<Conversion> = TODO()

}