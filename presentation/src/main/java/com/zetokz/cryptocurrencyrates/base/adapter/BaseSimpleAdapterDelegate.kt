package com.zetokz.cryptocurrencyrates.base.adapter

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.zetokz.cryptocurrencyrates.ui.model.Identifiable

/**
 * Created by Yevhenii Rechun on 11/4/17.
 * Copyright © 2017. All rights reserved.
 */
abstract class BaseSimpleAdapterDelegate<VH : BaseViewHolder<I>, I : Identifiable> :
    AbsListItemAdapterDelegate<I, Identifiable, VH>() {

    protected var clickListener: OnItemClickListener<I>? = null
    protected var useClickListenerForRootItem = true

    fun setOnItemClickListener(clickListener: OnItemClickListener<I>) {
        this.clickListener = clickListener
    }

    fun setOnItemClickListener(
        actionWithPosition: ((position: Int, item: I) -> Unit)? = null,
        actionOnlyItem: ((item: I) -> Unit)? = null,
        actionWithViewHolder: ((viewHolder: VH, item: I) -> Unit)? = null
    ) {
        this.clickListener = object : OnItemClickListener<I> {
            override fun onItemClicked(viewHolder: ViewHolder, item: I) {
                actionWithPosition?.invoke(viewHolder.adapterPosition, item)
                actionOnlyItem?.invoke(item)
                @Suppress("UNCHECKED_CAST")
                actionWithViewHolder?.invoke(viewHolder as VH, item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup): VH = if (getItemResId() != 0) {
        createViewHolder(LayoutInflater.from(parent.context).inflate(getItemResId(), parent, false))
    } else {
        throw IllegalArgumentException("Item resource id must not be null")
    }

    override fun onBindViewHolder(item: I, holder: VH, payloads: List<Any>) {
        onBindViewHolder(holder, item, clickListener)
        if (clickListener != null && useClickListenerForRootItem) {
            holder.itemView.setOnClickListener {
                clickListener?.onItemClicked(holder, item)
            }
        }
    }

    protected open fun onBindViewHolder(holder: VH, item: I) {
        //for child implementations
        holder.bind(item)
    }

    protected open fun onBindViewHolder(holder: VH, item: I, clickListener: OnItemClickListener<I>?) {
        onBindViewHolder(holder, item)
    }

    protected abstract fun createViewHolder(itemView: View): VH

    @LayoutRes
    protected abstract fun getItemResId(): Int

}
