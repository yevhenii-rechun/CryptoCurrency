package com.zetokz.cryptocurrencyrates.ui.model

/**
 * Created by Yevhenii Rechun on 3/11/18.
 * Copyright © 2017. All rights reserved.
 */
data class CurrencyItemSelectable(
    val iconLink: String?,
    val shortName: String,
    val fullName: String,
    var isSelected: Boolean
) : Identifiable {
    override val id = (fullName.hashCode() + shortName.hashCode()).toLong()
}