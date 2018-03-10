package com.zetokz.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.database.Cursor
import com.zetokz.data.database.dao.CurrencyDao
import com.zetokz.data.model.Currency
import org.intellij.lang.annotations.Language
import javax.inject.Singleton

/**
 * Created by Yevhenii Rechun on 10/31/17.
 * Copyright © 2017. All rights reserved.
 */
@Singleton
@Database(
    entities = [
        Currency::class
    ],
    version = 1
)
internal abstract class CryptoExchangeDatabase : RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao

    @Synchronized fun dropAllTables() = runInTransaction {
        @Language("RoomSql")
        val cursor: Cursor = query(
            """SELECT name FROM sqlite_master WHERE type IS 'table'
            AND name NOT IN (
            'sqlite_master',
            'sqlite_sequence',
            'android_metadata',
            'room_master_table'
            )""", null
        )
        val names = mutableListOf<String>()
        while (cursor.moveToNext()) names.add(cursor.getString(0))
        names.onEach { openHelper.writableDatabase.delete(it, null, null) }
    }
}