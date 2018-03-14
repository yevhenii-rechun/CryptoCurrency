package com.zetokz.data.network

import com.zetokz.data.model.CurrencyRateResponse
import io.reactivex.Single
import retrofit2.http.GET

/**
 * Created by Yevhenii Rechun on 1/17/18.
 * Copyright © 2017. All rights reserved.
 */
internal interface CurrencyRateService {

    //https://www.cryptocompare.com/api/data/coinlist/
    @GET("api/data/coinlist")
    fun fetchAvailableCurrencies(): Single<CurrencyRateResponse>
}