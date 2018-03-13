package com.zetokz.cryptocurrencyrates.ui.chosencurrencies.adapter

import com.zetokz.cryptocurrencyrates.base.adapter.SimpleListIdentifiableAdapter
import com.zetokz.cryptocurrencyrates.ui.model.CurrencyItem
import com.zetokz.cryptocurrencyrates.ui.model.Identifiable

/**
 * Created by Yevhenii Rechun on 11/27/17.
 * Copyright © 2017. All rights reserved.
 */
class ChosenCurrenciesAdapter(
    onCurrencyClickedAction: (CurrencyItem) -> Unit
) : SimpleListIdentifiableAdapter<Identifiable>() { //todo: need to test performance with QueueAdapter

    init {
        delegatesManager.addDelegate(ChosenCurrenciesAdapterDelegate().apply {
            setOnItemClickListener(actionOnlyItem = onCurrencyClickedAction)
        })
    }
}