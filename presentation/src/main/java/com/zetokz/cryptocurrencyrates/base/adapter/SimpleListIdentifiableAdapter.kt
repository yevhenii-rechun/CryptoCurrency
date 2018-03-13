package com.zetokz.cryptocurrencyrates.base.adapter

import android.support.v7.util.DiffUtil
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import com.zetokz.cryptocurrencyrates.ui.model.Identifiable

/**
 * Created by Yevhenii Rechun on 12/22/17.
 * Copyright © 2017. All rights reserved.
 */
open class SimpleListIdentifiableAdapter<T : Identifiable> : ListDelegationAdapter<List<T>>() {

    init {
        items = listOf()
    }

    fun dispatchNewItems(items: List<T>) {
        val diff = DiffUtil.calculateDiff(SimpleDiffUtilCallback(this.items, items))
        this.items = items
        diff.dispatchUpdatesTo(this)
    }
}