package com.zetokz.data.network

import com.zetokz.data.Config
import com.zetokz.data.model.ConversionResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Yevhenii Rechun on 3/13/18.
 * Copyright © 2017. All rights reserved.
 */
interface ExchangeRateService {

    //https://min-api.cryptocompare.com/data/price?tsyms=BTC,USD,EUR,ETH&fsym=ETH
    @GET("price?tsyms=${Config.EXPECTED_CONVERSION_CURRENCY}")
    fun fetchExchangeRate(@Query("fsym") baseCurrency: String): Single<ConversionResponse>
}