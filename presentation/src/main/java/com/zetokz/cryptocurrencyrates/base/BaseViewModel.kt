package com.zetokz.cryptocurrencyrates.base

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    protected val disposables: CompositeDisposable by lazy { CompositeDisposable() }

    internal fun handleCommonError(throwable: Throwable) = throwable.printStackTrace()

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}
