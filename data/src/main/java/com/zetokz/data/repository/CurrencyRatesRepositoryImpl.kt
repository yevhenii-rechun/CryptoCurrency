package com.zetokz.data.repository

import com.zetokz.data.database.dao.CurrencyDao
import com.zetokz.data.model.Currency
import com.zetokz.data.network.CurrencyRateService
import dagger.Reusable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Yevhenii Rechun on 1/16/18.
 * Copyright © 2017. All rights reserved.
 */
@Reusable
internal class CurrencyRatesRepositoryImpl @Inject constructor(
    private val currencyRateService: CurrencyRateService,
    private val currencyDao: CurrencyDao
) : CurrencyRatesRepository {

    override fun getAvailableCurrencies(): Single<List<Currency>> =
        currencyRateService.fetchAvailableCurrencies()
            .map { it.data.asSequence().map { it.value }.toList() }
            .subscribeOn(Schedulers.io())

    override fun getChosenCurrencies(): Single<List<Currency>> = currencyDao.findAll()
        .subscribeOn(Schedulers.io())
}