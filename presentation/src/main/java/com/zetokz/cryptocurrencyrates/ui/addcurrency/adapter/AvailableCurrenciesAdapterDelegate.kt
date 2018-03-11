package com.zetokz.cryptocurrencyrates.ui.addcurrency.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import bindView
import com.zetokz.cryptocurrencyrates.R
import com.zetokz.cryptocurrencyrates.base.adapter.BaseSimpleAdapterDelegate
import com.zetokz.cryptocurrencyrates.base.adapter.BaseViewHolder
import com.zetokz.cryptocurrencyrates.ui.model.CurrencyItemSelectable
import com.zetokz.cryptocurrencyrates.ui.model.Identifiable
import com.zetokz.cryptocurrencyrates.util.extension.changeVisibility
import com.zetokz.cryptocurrencyrates.util.extension.loadImage

/**
 * Created by Yevhenii Rechun on 1/16/18.
 * Copyright © 2017. All rights reserved.
 */
internal class AvailableCurrenciesAdapterDelegate :
    BaseSimpleAdapterDelegate<AvailableCurrenciesAdapterDelegate.AvailableCurrencyRateViewHolder, CurrencyItemSelectable>() {

    override fun isForViewType(item: Identifiable, items: MutableList<Identifiable>, position: Int) =
        item is CurrencyItemSelectable

    override fun createViewHolder(itemView: View) = AvailableCurrencyRateViewHolder(itemView)

    override fun getItemResId() = R.layout.item_currency_selectable

    inner class AvailableCurrencyRateViewHolder(view: View) : BaseViewHolder<CurrencyItemSelectable>(view) {

        private val iconCurrencyLogo: ImageView by bindView(R.id.icon_crypto_logo)
        private val textCurrencyShortName: TextView by bindView(R.id.text_currency_name_short)
        private val textCurrencyFullName: TextView by bindView(R.id.text_currency_name_full)
        private val labelChosenCurrency: TextView by bindView(R.id.label_chosen_currency)

        override fun bind(item: CurrencyItemSelectable) {
            with(item) {
                iconLink?.let { iconCurrencyLogo.loadImage(it) }
                textCurrencyShortName.text = shortName
                textCurrencyFullName.text = fullName
                labelChosenCurrency.changeVisibility(isSelected)
            }
        }
    }
}