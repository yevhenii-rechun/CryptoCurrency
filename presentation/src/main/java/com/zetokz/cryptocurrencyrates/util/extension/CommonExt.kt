package com.zetokz.cryptocurrencyrates.util.extension

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Build
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.text.Spanned
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

fun Any?.notEquals(other: Any?): Boolean = this?.equals(other)?.not() ?: false

fun Boolean.ifFalse(block: () -> Unit) {
    if (!this) {
        block()
    }
}

fun String.toSpannedHtml(): Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
} else {
    @Suppress("DEPRECATION")
    Html.fromHtml(this)
}