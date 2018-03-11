package com.zetokz.cryptocurrencyrates.base

import android.support.v7.app.AppCompatActivity
import com.zetokz.cryptocurrencyrates.R
import com.zetokz.cryptocurrencyrates.injection.Injectable
import com.zetokz.cryptocurrencyrates.util.extension.hideBlockingProgressDialog
import com.zetokz.cryptocurrencyrates.util.extension.showBlockingProgressDialog
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Yevhenii Rechun on 1/16/18.
 * Copyright © 2017. All rights reserved.
 */
abstract class BaseActivity : AppCompatActivity(), Injectable {

    protected val disposables: CompositeDisposable by lazy { CompositeDisposable() }

    fun showBlockingProgress() {
        showBlockingProgressDialog(getString(R.string.loading))
    }

    fun hideBlockingProgress() {
        hideBlockingProgressDialog()
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}