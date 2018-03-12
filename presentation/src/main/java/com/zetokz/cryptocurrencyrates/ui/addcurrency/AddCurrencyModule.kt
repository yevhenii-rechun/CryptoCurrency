package com.zetokz.cryptocurrencyrates.ui.addcurrency

import android.arch.lifecycle.ViewModel
import com.zetokz.cryptocurrencyrates.injection.ActivityScope
import com.zetokz.cryptocurrencyrates.injection.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

/**
 * Created by Yevhenii Rechun on 3/11/18.
 * Copyright © 2017. All rights reserved.
 */
@Module
abstract class AddCurrencyModule {

    @Binds @IntoMap @ActivityScope
    @ViewModelKey(AddCurrencyViewModel::class)
    abstract fun bindsAddCurrencyViewModel(addCurrencyViewModel: AddCurrencyViewModel): ViewModel

    @Module
    companion object {

        @Provides @ActivityScope @JvmStatic
        fun provideAddCurrencyRouter(addCurrencyActivity: AddCurrencyActivity): AddCurrencyRouter =
            addCurrencyActivity
    }

}