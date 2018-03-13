package com.zetokz.data.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import com.zetokz.data.model.Currency
import io.reactivex.Flowable

/**
 * Created by Yevhenii Rechun on 11/28/17.
 * Copyright © 2017. All rights reserved.
 */
@Dao
internal abstract class CurrencyDao : BaseDao<Currency> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun bulkInsert(currencies: List<Currency>)

    @Query("DELETE FROM currencies")
    abstract fun deleteAll()

    @Query("SELECT * FROM currencies")
    abstract fun findAll(): Flowable<List<Currency>>

    @Query("DELETE FROM currencies WHERE id = :currencyId")
    abstract fun deleteCurrencyById(currencyId: Int)

    @Transaction open fun updateChosenCurrencies(chosenCurrencies: List<Currency>) {
        deleteAll()
        bulkInsert(chosenCurrencies)
    }
}