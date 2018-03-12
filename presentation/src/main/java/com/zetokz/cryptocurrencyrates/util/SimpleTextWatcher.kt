package com.zetokz.cryptocurrencyrates.util

import android.text.Editable
import android.text.TextWatcher

/**
 * Created by Yevhenii Rechun on 1/16/18.
 * Copyright © 2017. All rights reserved.
 */
class SimpleTextWatcher(
    private val beforeTextChangedAction: SimpleTextWatcher.(s: CharSequence?, start: Int, count: Int, after: Int) -> Unit = { _, _, _, _ -> },
    private val onTextChangedAction: SimpleTextWatcher.(s: CharSequence?, start: Int, before: Int, count: Int) -> Unit = { _, _, _, _ -> },
    private val afterTextChangedAction: SimpleTextWatcher.(s: Editable?) -> Unit = { _ -> }
) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        beforeTextChangedAction(this, s, start, count, after)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        onTextChangedAction(this, s, start, before, count)
    }

    override fun afterTextChanged(s: Editable?) {
        afterTextChangedAction(this, s)
    }
}