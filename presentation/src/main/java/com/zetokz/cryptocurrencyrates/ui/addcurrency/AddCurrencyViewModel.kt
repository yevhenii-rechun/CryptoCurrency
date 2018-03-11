package com.zetokz.cryptocurrencyrates.ui.addcurrency

import com.zetokz.cryptocurrencyrates.base.BaseViewModel
import com.zetokz.cryptocurrencyrates.ui.model.CurrencyItemSelectable
import com.zetokz.cryptocurrencyrates.ui.model.toCurrencySelectableItems
import com.zetokz.data.interactor.CurrencyRatesInteractor
import com.zetokz.data.model.Currency
import io.reactivex.Completable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

/**
 * Created by Yevhenii Rechun on 3/10/18.
 * Copyright © 2017. All rights reserved.
 */
class AddCurrencyViewModel @Inject constructor(
    currencyRatesInteractor: CurrencyRatesInteractor
) : BaseViewModel() {

    val currenciesData = BehaviorSubject.create<List<CurrencyItemSelectable>>()
    val currencyItemSelect = PublishSubject.create<CurrencyItemSelectable>()
    val clickSave = PublishSubject.create<Boolean>()

    private lateinit var rawCurrency: List<Currency>
    private val currenciesToSave = mutableListOf<Currency>()

    init {

        //todo merge with current selected currencies
        currencyRatesInteractor.getAvailableCurrencies()
            .subscribeOn(Schedulers.io())
            .doOnSuccess { if (it.isEmpty()) throw EmptyDataError() else rawCurrency = it }
            .map { it.toCurrencySelectableItems() }
            .subscribe(currenciesData::onNext, currenciesData::onError)
            .addTo(disposables)

        currencyItemSelect
            .subscribeOn(Schedulers.io())
            .doOnNext { item ->
                if (!item.isSelected) {
                    rawCurrency.firstOrNull { it.name == item.shortName }
                        ?.let(currenciesToSave::add)
                } else {
                    currenciesToSave.firstOrNull { it.name == item.shortName }
                        ?.let(currenciesToSave::remove)
                }
                currenciesData.value.first(item::equals).isSelected = !item.isSelected
            }
            .map { currenciesData.value }
            .subscribe(currenciesData::onNext, ::handleCommonError)
            .addTo(disposables)

        clickSave.subscribeOn(Schedulers.io())
            .flatMapCompletable {
                if (it) currencyRatesInteractor.saveCurrencies(currenciesToSave)
                else Completable.complete()
            }
            .subscribe(
                {
                    print("")
                }, {
                    println()
                }
            )
            .addTo(disposables)
    }

}

class EmptyDataError : Exception()