package com.zetokz.cryptocurrencyrates.util.extension

import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * disposable -= observable.subscribe()
 */
private const val DEBOUNCE_DELAY_MS = 300L

operator fun CompositeDisposable.minusAssign(disposable: Disposable?) {
    if (disposable != null) remove(disposable)
}

fun <T : Any> Flowable<T>.applyThrottling(): Flowable<T> = compose(applyThrottlingFlowable<T>())

fun <T : Any> Observable<T>.applyThrottling(): Observable<T> = compose(applyThrottlingObservable<T>())

private fun <T : Any> applyThrottlingObservable(): ObservableTransformer<T, T> = ObservableTransformer {
    it.throttleFirst(DEBOUNCE_DELAY_MS, TimeUnit.MILLISECONDS)
}

private fun <T : Any> applyThrottlingFlowable(): FlowableTransformer<T, T> = FlowableTransformer {
    it.throttleFirst(DEBOUNCE_DELAY_MS, TimeUnit.MILLISECONDS)
}

fun <T> Observable<T>.subscribeNoError(
    onNext: (T) -> Unit,
    onError: (Throwable) -> Unit = { it.printStackTrace() }
): Disposable = subscribe(onNext, onError)

