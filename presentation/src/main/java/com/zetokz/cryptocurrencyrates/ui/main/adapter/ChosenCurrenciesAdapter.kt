package com.zetokz.cryptocurrencyrates.ui.main.adapter

import com.zetokz.cryptocurrencyrates.base.adapter.SimpleListIdentifiableAdapter
import com.zetokz.cryptocurrencyrates.ui.model.CurrencyItem

/**
 * Created by Yevhenii Rechun on 11/27/17.
 * Copyright © 2017. All rights reserved.
 */
class ChosenCurrenciesAdapter(
    onCurrencyClickedAction: (CurrencyItem) -> Unit
) : SimpleListIdentifiableAdapter() { //todo: need to test performance with QueueAdapter

    init {
        delegatesManager.addDelegate(ChosenCurrenciesAdapterDelegate().apply {
            setOnItemClickListener(actionOnlyItem = onCurrencyClickedAction)
        })
    }
}