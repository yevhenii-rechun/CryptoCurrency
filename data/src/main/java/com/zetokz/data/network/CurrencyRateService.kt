package com.zetokz.data.network

import com.zetokz.data.Config
import com.zetokz.data.model.ConversionResponse
import com.zetokz.data.model.CurrencyRateResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Yevhenii Rechun on 1/17/18.
 * Copyright © 2017. All rights reserved.
 */
internal interface CurrencyRateService {

    //https://www.cryptocompare.com/api/data/coinlist/
    @GET("api/data/coinlist")
    fun fetchAvailableCurrencies(): Single<CurrencyRateResponse>

    //https://min-api.cryptocompare.com/data/price?tsyms=BTC,USD,EUR,ETH&fsym=ETH
    @GET("data/price?tsyms=${Config.EXPECTED_CONVERSION_CURRENCY}")
    fun fetchExchangeRate(@Query("fsym") baseCurrency: String): Single<ConversionResponse>
}