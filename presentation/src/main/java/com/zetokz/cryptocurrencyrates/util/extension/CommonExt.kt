package com.zetokz.cryptocurrencyrates.util.extension

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import com.zetokz.cryptocurrencyrates.base.BaseViewModel


fun FragmentActivity.currentFragment() = supportFragmentManager.fragments?.filter { it.isVisible }?.firstOrNull()

fun <T> List<T>.hasElement(predicate: (T) -> Boolean) = firstOrNull(predicate) != null

inline fun <reified T : Fragment> AppCompatActivity.findFragmentByType() = supportFragmentManager.fragments
    ?.filter { it is T }
    ?.map { it as T }
    ?.firstOrNull()

inline fun <reified T : BaseViewModel> FragmentActivity.getViewModel(
    factory: ViewModelProvider.Factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
): T = ViewModelProviders.of(this, factory).get(T::class.java)

inline fun <reified T : BaseViewModel> Fragment.getViewModel(
    factory: ViewModelProvider.Factory = ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
): T = ViewModelProviders.of(this, factory).get(T::class.java)

fun Boolean.ifTrue(block: () -> Unit) {
    if (this) {
        block()
    }
}

fun Boolean.ifFalse(block: () -> Unit) {
    if (!this) {
        block()
    }
}