package com.zetokz.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Yevhenii Rechun on 1/17/18.
 * Copyright © 2017. All rights reserved.
 */
@Entity(tableName = "currencies")
data class Currency(
    @PrimaryKey
    val id: String,
    val url: String,
    val imageUrl: String,
    val name: String,
    val symbol: String,
    val coinName: String,
    val fullName: String,
    val algorithm: String,
    val proofType: String,
    val fullyPremined: String,
    val totalCoinSupply: String,
    val preminedValue: String?,
    val totalCoinsFreeFloat: String,
    val sortOrder: String,
    val sponsored: Boolean
)