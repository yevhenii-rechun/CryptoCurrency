package com.zetokz.cryptocurrencyrates.base.adapter

import android.os.Handler
import android.support.v7.util.DiffUtil
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import com.zetokz.cryptocurrencyrates.ui.model.Identifiable
import java.util.ArrayDeque
import java.util.Deque


/**
 * Created by Yevhenii Rechun on 1/18/18.
 * Copyright © 2017. All rights reserved.
 */
open class QueueAdapter : ListDelegationAdapter<List<Identifiable>>() {

    private val pendingUpdates: Deque<List<Identifiable>> = ArrayDeque()

    init {
        items = listOf()
    }

    private fun updateItemsInternal(newItems: List<Identifiable>) {
        val oldItems = items.toList()
        val handler = Handler()
        Thread(Runnable {
            val diffResult = DiffUtil.calculateDiff(SimpleDiffUtilCallback(oldItems, newItems))
            handler.post { applyDiffResult(newItems, diffResult) }
        }).start()
    }

    private fun updateItems(newItems: List<Identifiable>) {
        pendingUpdates.push(newItems)
        if (pendingUpdates.size > 1) return
        updateItemsInternal(newItems)
    }

    protected fun dispatchUpdates(
        newItems: List<Identifiable>,
        diffResult: DiffUtil.DiffResult
    ) {
        diffResult.dispatchUpdatesTo(this)
        items = newItems
    }

    protected fun applyDiffResult(
        newItems: List<Identifiable>,
        diffResult: DiffUtil.DiffResult
    ) {
        pendingUpdates.remove(newItems)
        dispatchUpdates(newItems, diffResult)
        if (pendingUpdates.size > 0) {
            val latest = pendingUpdates.pop()
            pendingUpdates.clear()
            updateItemsInternal(latest)
        }
    }

    fun dispatchNewItems(items: List<Identifiable>) {
        updateItems(items)
    }
}