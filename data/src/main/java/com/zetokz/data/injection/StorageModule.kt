package com.zetokz.data.injection

import android.arch.persistence.room.Room
import android.content.Context
import android.content.SharedPreferences
import com.zetokz.data.database.CryptoExchangeDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Yevhenii Rechun on 3/10/18.
 * Copyright © 2017. All rights reserved.
 */
@Module
class StorageModule {

    @Provides @Singleton
    internal fun provideSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences("magri-preferences", Context.MODE_PRIVATE)

    @Provides @Singleton
    internal fun provideDatabase(context: Context): CryptoExchangeDatabase = Room
        .databaseBuilder(context, CryptoExchangeDatabase::class.java, "crypto-exchange-database")
        .fallbackToDestructiveMigration() //TODO add database migration
        .build()

    @Provides internal fun provideCurrencyDao(database: CryptoExchangeDatabase) = database.currencyDao()
}