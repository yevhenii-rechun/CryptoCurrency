package com.zetokz.cryptocurrencyrates.ui.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Yevhenii Rechun on 3/10/18.
 * Copyright © 2017. All rights reserved.
 */
data class CurrencyItem(
    val iconLink: String?,
    val shortName: String,
    val fullName: String
) : Identifiable, Parcelable {
    override val id = (fullName.hashCode() + shortName.hashCode()).toLong()

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(iconLink)
        parcel.writeString(shortName)
        parcel.writeString(fullName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CurrencyItem> {
        override fun createFromParcel(parcel: Parcel): CurrencyItem {
            return CurrencyItem(parcel)
        }

        override fun newArray(size: Int): Array<CurrencyItem?> {
            return arrayOfNulls(size)
        }
    }
}