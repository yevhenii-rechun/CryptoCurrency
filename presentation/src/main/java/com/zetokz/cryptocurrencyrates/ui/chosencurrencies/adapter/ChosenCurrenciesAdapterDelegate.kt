package com.zetokz.cryptocurrencyrates.ui.chosencurrencies.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import bindView
import com.zetokz.cryptocurrencyrates.R
import com.zetokz.cryptocurrencyrates.base.adapter.BaseSimpleAdapterDelegate
import com.zetokz.cryptocurrencyrates.base.adapter.BaseViewHolder
import com.zetokz.cryptocurrencyrates.ui.model.CurrencyItem
import com.zetokz.cryptocurrencyrates.ui.model.Identifiable
import com.zetokz.cryptocurrencyrates.util.extension.loadImage

/**
 * Created by Yevhenii Rechun on 1/16/18.
 * Copyright © 2017. All rights reserved.
 */
internal class ChosenCurrenciesAdapterDelegate :
    BaseSimpleAdapterDelegate<ChosenCurrenciesAdapterDelegate.CurrencyRateViewHolder, CurrencyItem>() {

    override fun isForViewType(item: Identifiable, items: MutableList<Identifiable>, position: Int) =
        item is CurrencyItem

    override fun createViewHolder(itemView: View) = CurrencyRateViewHolder(itemView)

    override fun getItemResId() = R.layout.item_currency

    inner class CurrencyRateViewHolder(view: View) : BaseViewHolder<CurrencyItem>(view) {

        private val iconCurrencyLogo: ImageView by bindView(R.id.icon_crypto_logo)
        private val textCurrencyShortName: TextView by bindView(R.id.text_currency_name_short)
        private val textCurrencyFullName: TextView by bindView(R.id.text_currency_name_full)

        override fun bind(item: CurrencyItem) {
            with(item) {
                iconLink?.let { iconCurrencyLogo.loadImage(it) }
                textCurrencyShortName.text = shortName
                textCurrencyFullName.text = fullName
            }
        }
    }
}