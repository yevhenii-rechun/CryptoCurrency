package com.zetokz.cryptocurrencyrates.base.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.zetokz.cryptocurrencyrates.ui.model.Identifiable

/**
 * Created by Yevhenii Rechun on 1/17/18.
 * Copyright © 2017. All rights reserved.
 */
abstract class BaseViewHolder<in I : Identifiable>(view: View) : RecyclerView.ViewHolder(view) {

    abstract fun bind(item: I)
}