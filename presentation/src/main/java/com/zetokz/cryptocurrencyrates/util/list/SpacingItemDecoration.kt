package com.zetokz.cryptocurrencyrates.util.list

import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View

class SpacingItemDecoration private constructor(
    private val betweenItems: Boolean,
    private val onSides: Boolean,
    private val onTop: Boolean,
    private val dividerSize: Int,
    private val applyTo: (viewHolder: ViewHolder?) -> Boolean = { true }
) : RecyclerView.ItemDecoration() {

    companion object {
        fun create(init: Builder.() -> Unit) = Builder(init).build()
    }

    private constructor(builder: Builder) : this(
        builder.showBetween,
        builder.showOnSides,
        builder.onTop,
        builder.spaceSize,
        builder.applyTo
    )

    private var orientation = -1

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildLayoutPosition(view)
        if (position == RecyclerView.NO_POSITION) return

        getOrientation(parent)

        if (orientation == OrientationHelper.HORIZONTAL) {
            if (applyTo(parent.findViewHolderForLayoutPosition(position))) {
                applyToHorizontalList(outRect)
            }
        } else {
            if (applyTo(parent.findViewHolderForLayoutPosition(position))) {
                applyToVerticalList(outRect)
            }
        }
    }

    private fun applyToVerticalList(outRect: Rect) {
        if (betweenItems) {
            outRect.top = if (onTop) dividerSize / 2 else 0
            outRect.bottom = dividerSize / 2
        }

        if (onSides) {
            outRect.left = dividerSize
            outRect.right = dividerSize
        }
    }

    private fun applyToHorizontalList(outRect: Rect) {
        if (betweenItems) {
            outRect.left = dividerSize / 2
            outRect.right = dividerSize / 2
        }

        if (onSides) {
            outRect.top = if (onTop) dividerSize else 0
            outRect.bottom = dividerSize
        }
    }

    private fun getOrientation(parent: RecyclerView) {
        if (orientation != -1) return
        if (parent.layoutManager is LinearLayoutManager) {
            val layoutManager = parent.layoutManager as LinearLayoutManager
            orientation = layoutManager.orientation
        } else {
            throw IllegalStateException("SpacingItemDecoration can only be used with a LinearLayoutManager.")
        }
    }

    class Builder private constructor() {

        constructor(init: Builder.() -> Unit) : this() {
            init()
        }

        var showBetween = false
        var showOnSides = false
        var onTop = true
        var applyTo: (viewHolder: ViewHolder?) -> Boolean = { true }
        var spaceSize: Int = 0

        fun build() = SpacingItemDecoration(this)
    }
}