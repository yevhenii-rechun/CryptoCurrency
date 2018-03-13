package com.zetokz.cryptocurrencyrates.util.extension

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import com.bumptech.glide.Glide
import com.kennyc.view.MultiStateView
import com.zetokz.cryptocurrencyrates.util.ProgressBarController

/**
 * Created by Yevhenii Rechun on 1/18/18.
 * Copyright © 2017. All rights reserved.
 */

internal fun SearchView.onQueryTextChanged(
    queryTextChangedFunc: (newQuery: String?) -> Unit,
    isActionPerformedByListener: Boolean = true
) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?) = true

        override fun onQueryTextChange(newText: String?): Boolean {
            queryTextChangedFunc(newText)
            return isActionPerformedByListener
        }
    })
}

internal fun Context.showBlockingProgressDialog(message: String? = null) {
    ProgressBarController.showProgressDialog(this, message)
}

internal fun Context.hideBlockingProgressDialog() {
    ProgressBarController.hideProgressDialog()
}

internal fun View.changeVisibility(visible: Boolean, keep: Boolean = false) {
    visibility = when {
        visible -> View.VISIBLE
        !visible && keep -> View.INVISIBLE
        !visible && !keep -> View.GONE
        else -> throw IllegalStateException("Illegal state")
    }
}

internal fun ImageView.loadImage(imageUrl: String) {
    Glide.with(this)
        .load(imageUrl)
        .into(this)
}

internal fun MultiStateView.showLoadingState() {
    viewState = MultiStateView.VIEW_STATE_LOADING
}

internal fun MultiStateView.showContentState() {
    viewState = MultiStateView.VIEW_STATE_CONTENT
}

internal fun MultiStateView.showEmptyState() {
    viewState = MultiStateView.VIEW_STATE_EMPTY
}

internal fun MultiStateView.showErrorState() {
    viewState = MultiStateView.VIEW_STATE_ERROR
}