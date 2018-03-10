package com.zetokz.data.network

import io.reactivex.Completable
import retrofit2.http.GET

/**
 * Created by Yevhenii Rechun on 1/18/18.
 * Copyright © 2017. All rights reserved.
 */
internal interface HealthCheckerService {

    @GET("/")
    fun fetchHostAlive(): Completable
}