package com.zetokz.cryptocurrencyrates.ui.main

import android.arch.lifecycle.ViewModel
import com.zetokz.cryptocurrencyrates.injection.ActivityScope
import com.zetokz.cryptocurrencyrates.injection.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

/**
 * Created by Yevhenii Rechun on 3/10/18.
 * Copyright © 2017. All rights reserved.
 */
@Module
abstract class ChosenCurrenciesModule {

    @Binds @IntoMap @ActivityScope
    @ViewModelKey(ChosenCurrenciesViewModel::class)
    abstract fun bindsChosenCurrenciesViewModel(chosenCurrenciesViewModel: ChosenCurrenciesViewModel): ViewModel


    @Module
    companion object {

        @Provides @ActivityScope @JvmStatic
        fun provideChosenCurrenciesRouter(chosenCurrenciesActivity: ChosenCurrenciesActivity): ChosenCurrenciesRouter =
            chosenCurrenciesActivity
    }

}