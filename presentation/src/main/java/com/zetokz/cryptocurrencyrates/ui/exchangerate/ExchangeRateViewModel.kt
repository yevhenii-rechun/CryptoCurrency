package com.zetokz.cryptocurrencyrates.ui.exchangerate

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.ReplayRelay
import com.zetokz.cryptocurrencyrates.base.BaseViewModel
import com.zetokz.cryptocurrencyrates.ui.model.ConversionItem
import com.zetokz.cryptocurrencyrates.ui.model.CurrencyItem
import com.zetokz.cryptocurrencyrates.ui.model.toConversionItems
import com.zetokz.cryptocurrencyrates.util.extension.minusAssign
import com.zetokz.data.interactor.CurrencyRatesInteractor
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposables
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named


/**
 * Created by Yevhenii Rechun on 3/12/18.
 * Copyright © 2017. All rights reserved.
 */
class ExchangeRateViewModel @Inject constructor(
    @Named(ExchangeRateModule.QUALIFIER_CURRENCY_ITEM) private val currencyItem: CurrencyItem,
    private val currencyRatesInteractor: CurrencyRatesInteractor
) : BaseViewModel() {

    companion object {

        private const val REFRESH_INTERVAL = 30L //seconds
    }

    val conversionData: ReplayRelay<List<ConversionItem>> = ReplayRelay.create()
    val headerData: Single<CurrencyItem> = Single.just(currencyItem)

    val viewState: BehaviorRelay<ExchangeRateState> = BehaviorRelay.create()

    val refreshDataSwiped: PublishRelay<Boolean> = PublishRelay.create()

    private var loadExchangeRateDisposable = Disposables.empty()

    init {
        disposables += Observable.interval(0, REFRESH_INTERVAL, TimeUnit.SECONDS, Schedulers.computation())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { viewState.accept(ExchangeRateState.LOADING) }
            .subscribe({ updateExchangeRates() }, ::handleCommonError)
    }

    private fun updateExchangeRates() {
        disposables -= loadExchangeRateDisposable
        loadExchangeRateDisposable = currencyRatesInteractor.getExchangeRates(currencyItem.shortName)
            .subscribeOn(Schedulers.io())
            .map { it.toConversionItems() }
            .doOnSuccess { viewState.accept(ExchangeRateState.CONTENT) }
            .doOnError { viewState.accept(ExchangeRateState.ERROR) }
            .subscribe(conversionData::accept, ::handleCommonError)
        disposables += loadExchangeRateDisposable
    }

}

enum class ExchangeRateState {
    LOADING, CONTENT, ERROR
}