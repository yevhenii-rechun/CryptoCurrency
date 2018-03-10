package com.zetokz.data.repository

import com.zetokz.data.network.HealthCheckerService
import dagger.Reusable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Yevhenii Rechun on 1/18/18.
 * Copyright © 2017. All rights reserved.
 */
@Reusable
internal class HostHealthRepositoryImpl @Inject constructor(
    private val healthCheckerService: HealthCheckerService
) : HostHealthRepository {

    override fun isAlive(): Single<Boolean> = healthCheckerService.fetchHostAlive()
        .andThen(Single.just(true))
        .onErrorResumeNext { Single.just(false) }
        .subscribeOn(Schedulers.io())
}