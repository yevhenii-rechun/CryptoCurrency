package com.zetokz.cryptocurrencyrates.base

import android.support.v7.app.AppCompatActivity
import com.zetokz.cryptocurrencyrates.injection.Injectable
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Yevhenii Rechun on 1/16/18.
 * Copyright © 2017. All rights reserved.
 */
abstract class BaseActivity : AppCompatActivity(), Injectable {

    protected val disposables: CompositeDisposable by lazy { CompositeDisposable() }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    fun close() {
        finish()
    }
}