package com.zetokz.cryptocurrencyrates.ui.chosencurrencies

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.zetokz.cryptocurrencyrates.base.BaseViewModel
import com.zetokz.cryptocurrencyrates.ui.model.CurrencyItem
import com.zetokz.cryptocurrencyrates.ui.model.toCurrencyItems
import com.zetokz.cryptocurrencyrates.util.extension.applyThrottling
import com.zetokz.data.interactor.CurrencyRatesInteractor
import com.zetokz.data.model.Currency
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Yevhenii Rechun on 3/10/18.
 * Copyright © 2017. All rights reserved.
 */
class ChosenCurrenciesViewModel @Inject constructor(
    chosenCurrenciesRouter: ChosenCurrenciesRouter,
    currencyRatesInteractor: CurrencyRatesInteractor
) : BaseViewModel() {

    val viewState: BehaviorRelay<ChosenCurrenciesViewState> = BehaviorRelay.create()
    val currenciesData: BehaviorRelay<List<CurrencyItem>> = BehaviorRelay.create()

    val removeCurrency: PublishRelay<CurrencyItem> = PublishRelay.create<CurrencyItem>()
    val addClick: PublishRelay<Boolean> = PublishRelay.create<Boolean>()
    val currencyItemClick: PublishRelay<CurrencyItem> = PublishRelay.create<CurrencyItem>()

    init {
        disposables += currencyRatesInteractor.getChosenCurrencies()
            .subscribeOn(Schedulers.io())
            .distinctUntilChanged()
            .map(List<Currency>::toCurrencyItems)
            .doOnSubscribe { viewState.accept(ChosenCurrenciesViewState.LOADING) }
            .doOnNext {
                if (it.isEmpty()) viewState.accept(ChosenCurrenciesViewState.EMPTY)
                else viewState.accept(ChosenCurrenciesViewState.CONTENT)
            }
            .subscribe(currenciesData::accept, ::handleCommonError)

        disposables += addClick
            .subscribeOn(Schedulers.io())
            .applyThrottling()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ chosenCurrenciesRouter.navigateToAddCurrency() }, ::handleCommonError)

        disposables += currencyItemClick
            .subscribeOn(Schedulers.io())
            .applyThrottling()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(chosenCurrenciesRouter::navigateToCurrencyExchangeRates, ::handleCommonError)

        disposables += removeCurrency
            .subscribeOn(Schedulers.io())
            .flatMapCompletable { currencyRatesInteractor.removeCurrency(it.id.toInt()) }
            .subscribeBy(::handleCommonError)
    }
}

enum class ChosenCurrenciesViewState {
    LOADING, CONTENT, EMPTY
}