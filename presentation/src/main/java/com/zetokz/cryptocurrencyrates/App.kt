package com.zetokz.cryptocurrencyrates

import com.zetokz.cryptocurrencyrates.injection.AppComponent
import com.zetokz.cryptocurrencyrates.injection.DaggerAppComponent
import com.zetokz.cryptocurrencyrates.injection.applyAutoInjector
import dagger.android.support.DaggerApplication

/**
 * Created by Yevhenii Rechun on 3/10/18.
 * Copyright © 2017. All rights reserved.
 */
class App : DaggerApplication() {

    companion object {

        lateinit var instance: App
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        applyAutoInjector()

        instance = this
    }

    override fun applicationInjector() = DaggerAppComponent.builder()
        .application(this)
        .build().apply { appComponent = this }
}