package com.zetokz.cryptocurrencyrates.injection

import android.arch.lifecycle.ViewModelProvider
import com.zetokz.cryptocurrencyrates.base.ViewModelFactory
import com.zetokz.cryptocurrencyrates.ui.addcurrency.AddCurrencyActivity
import com.zetokz.cryptocurrencyrates.ui.addcurrency.AddCurrencyModule
import com.zetokz.cryptocurrencyrates.ui.chosencurrencies.ChosenCurrenciesActivity
import com.zetokz.cryptocurrencyrates.ui.chosencurrencies.ChosenCurrenciesModule
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Yevhenii Rechun on 1/16/18.
 * Copyright © 2017. All rights reserved.
 */
@Module
interface ActivitiesModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @ActivityScope @ContributesAndroidInjector(modules = [ChosenCurrenciesModule::class])
    fun contributeChosenCurrenciesActivity(): ChosenCurrenciesActivity

    @ActivityScope @ContributesAndroidInjector(modules = [AddCurrencyModule::class])
    fun contributeAddCurrencyActivity(): AddCurrencyActivity

//    @ActivityScope @ContributesAndroidInjector
//    fun contributeCurrencyDetailsActivity(): CurrencyDetailsActivity

//    @ActivityScope @ContributesAndroidInjector
//    fun contributeAddCurrencyActivity(): AddCurrencyActivity

}