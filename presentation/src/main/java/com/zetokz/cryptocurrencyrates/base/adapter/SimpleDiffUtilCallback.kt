package com.zetokz.cryptocurrencyrates.base.adapter

import android.support.v7.util.DiffUtil
import com.zetokz.cryptocurrencyrates.ui.model.Identifiable

/**
 * Created by Yevhenii Rechun on 11/27/17.
 * Copyright © 2017. All rights reserved.
 */
class SimpleDiffUtilCallback(
    private val oldData: List<Identifiable>,
    private val newData: List<Identifiable>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldData[oldItemPosition]
        val newItem = newData[newItemPosition]
        return oldItem.javaClass == newItem.javaClass && (oldItem.id == newItem.id)
    }

    override fun getOldListSize(): Int = oldData.size

    override fun getNewListSize(): Int = newData.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldData[oldItemPosition] == newData[newItemPosition]

}