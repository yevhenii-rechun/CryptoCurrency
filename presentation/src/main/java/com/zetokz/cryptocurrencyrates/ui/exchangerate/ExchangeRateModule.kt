package com.zetokz.cryptocurrencyrates.ui.exchangerate

import android.arch.lifecycle.ViewModel
import com.zetokz.cryptocurrencyrates.injection.ActivityScope
import com.zetokz.cryptocurrencyrates.injection.ViewModelKey
import com.zetokz.cryptocurrencyrates.ui.model.CurrencyItem
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Named

/**
 * Created by Yevhenii Rechun on 3/12/18.
 * Copyright © 2017. All rights reserved.
 */
@Module
abstract class ExchangeRateModule {

    @Binds @IntoMap @ActivityScope
    @ViewModelKey(ExchangeRateViewModel::class)
    abstract fun bindsExchangeRateViewModel(exchangeRateViewModel: ExchangeRateViewModel): ViewModel

    @Module
    companion object {

        const val QUALIFIER_CURRENCY_ITEM = "QUALIFIER_CURRENCY_ITEM"

        @Provides @ActivityScope @JvmStatic
        @Named(QUALIFIER_CURRENCY_ITEM)
        fun provideCurrencyItem(exchangeRateActivity: ExchangeRateActivity): CurrencyItem =
            exchangeRateActivity.currencyItem
    }

}