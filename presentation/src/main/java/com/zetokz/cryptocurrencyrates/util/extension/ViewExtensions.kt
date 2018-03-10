package com.zetokz.cryptocurrencyrates.util.extension

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.kennyc.view.MultiStateView

/**
 * Created by Yevhenii Rechun on 1/18/18.
 * Copyright © 2017. All rights reserved.
 */

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