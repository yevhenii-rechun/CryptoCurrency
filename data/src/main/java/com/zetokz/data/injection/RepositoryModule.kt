package com.zetokz.data.injection

import com.zetokz.data.repository.CurrencyRatesRepository
import com.zetokz.data.repository.CurrencyRatesRepositoryImpl
import com.zetokz.data.repository.HostHealthRepository
import com.zetokz.data.repository.HostHealthRepositoryImpl
import dagger.Binds
import dagger.Module

/**
 * Created by Yevhenii Rechun on 1/17/18.
 * Copyright © 2017. All rights reserved.
 */
@Module
abstract class RepositoryModule {

    @Binds
    internal abstract fun provideCurrencyRatesRepository(repository: CurrencyRatesRepositoryImpl): CurrencyRatesRepository

    @Binds
    internal abstract fun provideHostHealthRepository(repository: HostHealthRepositoryImpl): HostHealthRepository
}