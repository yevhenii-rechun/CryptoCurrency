package com.zetokz.cryptocurrencyrates.ui.main

import com.zetokz.cryptocurrencyrates.base.BaseViewModel
import com.zetokz.cryptocurrencyrates.ui.model.CurrencyItem
import com.zetokz.cryptocurrencyrates.ui.model.toCurrencyItems
import com.zetokz.data.interactor.CurrencyRatesInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

/**
 * Created by Yevhenii Rechun on 3/10/18.
 * Copyright © 2017. All rights reserved.
 */
class ChosenCurrenciesViewModel @Inject constructor(
    chosenCurrenciesRouter: ChosenCurrenciesRouter,
    currencyRatesInteractor: CurrencyRatesInteractor
) : BaseViewModel() {

    val currenciesData = BehaviorSubject.create<List<CurrencyItem>>()
    val addClick = PublishSubject.create<Boolean>()
    val currencyItemClick = PublishSubject.create<CurrencyItem>()

    init {
        currencyRatesInteractor.getChosenCurrencies()
            .subscribeOn(Schedulers.io())
            .map { it.toCurrencyItems() }
            .subscribe(currenciesData::onNext, ::handleCommonError)
            .addTo(disposables)

        addClick.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ chosenCurrenciesRouter.navigateToAddCurrency() }, { handleCommonError(it) })
            .addTo(disposables)

        currencyItemClick.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(chosenCurrenciesRouter::navigateToCurrencyExchangeRates, ::handleCommonError)
            .addTo(disposables)
    }

}