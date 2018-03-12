package com.zetokz.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Yevhenii Rechun on 1/17/18.
 * Copyright © 2017. All rights reserved.
 */
data class CurrencyRateResponse(
    @SerializedName("Response") val response: String,
    @SerializedName("Message") val message: String,
    @SerializedName("BaseImageUrl") val baseImageUrl: String, //todo rest config file can be configured according to this value
    @SerializedName("BaseLinkUrl") val baseLinkUrl: String,
    @SerializedName("Data") val data: HashMap<String, Currency>,
    @SerializedName("Type") val type: Int
)