package com.zetokz.cryptocurrencyrates.base.adapter

import android.support.v7.widget.RecyclerView.ViewHolder

/**
 * Created by Yevhenii Rechun on 11/3/17.
 * Copyright © 2017. All rights reserved.
 */
interface OnItemClickListener<in I> {

    fun onItemClicked(viewHolder: ViewHolder, item: I)
}