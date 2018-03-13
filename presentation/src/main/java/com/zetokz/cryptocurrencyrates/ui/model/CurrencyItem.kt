package com.zetokz.cryptocurrencyrates.ui.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Yevhenii Rechun on 3/10/18.
 * Copyright © 2017. All rights reserved.
 */
data class CurrencyItem(
    override val id: Long,
    val iconLink: String?,
    val shortName: String,
    val fullName: String
) : Identifiable, Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(iconLink)
        parcel.writeString(shortName)
        parcel.writeString(fullName)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<CurrencyItem> {
        override fun createFromParcel(parcel: Parcel) = CurrencyItem(parcel)

        override fun newArray(size: Int): Array<CurrencyItem?> = arrayOfNulls(size)
    }
}