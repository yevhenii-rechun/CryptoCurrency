package com.zetokz.data.database.dao

import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Update

/**
 * Created by Denys Nykyforov on 09.11.2017
 * Copyright (c) 2017. All right reserved
 */
internal interface BaseDao<in T> {

    @Insert fun insert(data: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(data: T): Long

    @Update fun update(data: T)

    @Delete fun delete(vararg data: T)
}