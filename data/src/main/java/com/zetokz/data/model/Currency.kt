package com.zetokz.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by Yevhenii Rechun on 1/17/18.
 * Copyright © 2017. All rights reserved.
 */
@Entity(tableName = "currencies")
data class Currency(
    @PrimaryKey
    val id: String,
    @SerializedName("Url") val url: String,
    @SerializedName("ImageUrl") val imageUrl: String,
    @SerializedName("Name") val name: String,
    @SerializedName("Symbol") val symbol: String,
    @SerializedName("CoinName") val coinName: String,
    @SerializedName("FullName") val fullName: String,
    @SerializedName("Algorithm") val algorithm: String,
    @SerializedName("ProofType") val proofType: String,
    @SerializedName("FullyPremined") val fullyPremined: String,
    @SerializedName("TotalCoinSupply") val totalCoinSupply: String,
    @SerializedName("PreMinedValue") val preminedValue: String,
    @SerializedName("TotalCoinsFreeFloat") val totalCoinsFreeFloat: String,
    @SerializedName("SortOrder") val sortOrder: String,
    @SerializedName("Sponsored") val sponsored: Boolean
)