package com.zetokz.cryptocurrencyrates.ui.addcurrency.adapter

import android.support.v7.util.DiffUtil
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import com.zetokz.cryptocurrencyrates.base.adapter.SimpleDiffUtilCallback
import com.zetokz.cryptocurrencyrates.ui.model.CurrencyItemSelectable
import com.zetokz.cryptocurrencyrates.ui.model.Identifiable

/**
 * Created by Yevhenii Rechun on 11/27/17.
 * Copyright © 2017. All rights reserved.
 */
class AvailableCurrenciesAdapter(
    onCurrencyClickedAction: (CurrencyItemSelectable) -> Unit
) : ListDelegationAdapter<List<Identifiable>>() { //todo: need to test performance with QueueAdapter

    init {
        items = listOf()
        delegatesManager.addDelegate(AvailableCurrenciesAdapterDelegate().apply {
            setOnItemClickListener(actionOnlyItem = onCurrencyClickedAction)
        })
    }

    fun dispatchNewItems(items: List<CurrencyItemSelectable>) {
        val diff = DiffUtil.calculateDiff(SimpleDiffUtilCallback(this.items, items))
        this.items = items
        diff.dispatchUpdatesTo(this)
    }
}