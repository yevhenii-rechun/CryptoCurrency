package com.zetokz.data.interactor

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import com.zetokz.data.repository.HostHealthRepository
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Yevhenii Rechun on 1/18/18.
 * Copyright © 2017. All rights reserved.
 */
class ConnectionInteractor @Inject constructor(
    private val context: Context,
    private val hostHealthCheckerRepository: HostHealthRepository
) {

    private companion object {
        private const val DEFAULT_REFRESH_PERIOD_SECONDS = 1L
    }

    fun observeInternetConnection(): Flowable<Boolean> =
        Flowable.interval(0, DEFAULT_REFRESH_PERIOD_SECONDS, TimeUnit.SECONDS, Schedulers.computation())
            .flatMap {
                isNetworkAvailable()
                    .flatMap { if (it) isHostAvailable() else Single.just(false) }
                    .toFlowable()
            }

    @SuppressLint("MissingPermission")
    private fun isNetworkAvailable() = Single.just(
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?)
            ?.activeNetworkInfo
            ?.isConnectedOrConnecting ?: false
    )
        .onErrorResumeNext { Single.just(false) }
        .subscribeOn(Schedulers.io())

    private fun isHostAvailable(): Single<Boolean> = hostHealthCheckerRepository.isAlive()
}