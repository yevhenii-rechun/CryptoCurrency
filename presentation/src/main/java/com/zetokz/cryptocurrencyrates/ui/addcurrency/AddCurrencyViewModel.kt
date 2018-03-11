package com.zetokz.cryptocurrencyrates.ui.addcurrency

import com.zetokz.cryptocurrencyrates.base.BaseViewModel
import com.zetokz.cryptocurrencyrates.ui.model.CurrencyItemSelectable
import com.zetokz.cryptocurrencyrates.ui.model.toCurrencySelectableItems
import com.zetokz.data.interactor.CurrencyRatesInteractor
import com.zetokz.data.model.Currency
import io.reactivex.Completable
import io.reactivex.rxkotlin.Singles
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
    addCurrencyRouter: AddCurrencyRouter,
    currencyRatesInteractor: CurrencyRatesInteractor
) : BaseViewModel() {

    val currenciesData = BehaviorSubject.create<List<CurrencyItemSelectable>>()
    val currencyItemSelect = PublishSubject.create<CurrencyItemSelectable>()
    val saveCurrencies = PublishSubject.create<Boolean>()
    val clickBack = PublishSubject.create<Boolean>()

    private lateinit var rawCurrency: List<Currency>
    private var needToShowSaveChangesDialog: Boolean = false
    private val currenciesToSave = mutableListOf<Currency>()

    init {
        loadCurrencies(currencyRatesInteractor)
            .subscribeOn(Schedulers.io())
            .doOnSuccess { if (it.isEmpty()) throw EmptyDataError() }
            .subscribe(currenciesData::onNext, currenciesData::onError)
            .addTo(disposables)

        currencyItemSelect
            .subscribeOn(Schedulers.io())
            .doOnNext(::saveAndChangeSelection)
            .map { currenciesData.value }
            .subscribe(currenciesData::onNext, ::handleCommonError)
            .addTo(disposables)

        saveCurrencies.subscribeOn(Schedulers.io())
            .flatMapCompletable {
                (if (it) currencyRatesInteractor.saveCurrencies(currenciesToSave) else Completable.complete())
                    .doOnComplete(saveCurrencies::onComplete)
                //todo verify this strange behavior
            }
            .subscribe(addCurrencyRouter::close, ::handleCommonError)
            .addTo(disposables)

        clickBack.subscribeOn(Schedulers.io())
            .map { needToShowSaveChangesDialog }
            .subscribe({ isNeedToShowDialog ->
                if (isNeedToShowDialog) addCurrencyRouter.showCloseDialog()
                else addCurrencyRouter.close()
            }, ::handleCommonError)
            .addTo(disposables)
    }

    private fun loadCurrencies(currencyRatesInteractor: CurrencyRatesInteractor) = Singles.zip(
        currencyRatesInteractor.getAvailableCurrencies()
            .doOnSuccess { rawCurrency = it }
            .map { it.toCurrencySelectableItems() },
        currencyRatesInteractor.getChosenCurrencies()
            .first(listOf())
            .doOnSuccess { currenciesToSave.addAll(it) }
            .map { it.toCurrencySelectableItems() }
    )
        .map { (availableCurrencies, savedCurrencies) ->
            availableCurrencies.apply {
                forEach { it.isSelected = savedCurrencies.firstOrNull(it::equals) != null }
            }
        }

    private fun saveAndChangeSelection(item: CurrencyItemSelectable) {
        needToShowSaveChangesDialog = true
        if (!item.isSelected) {
            rawCurrency.firstOrNull { it.name == item.shortName }
                ?.let(currenciesToSave::add)
        } else {
            currenciesToSave.firstOrNull { it.name == item.shortName }
                ?.let(currenciesToSave::remove)
        }
        currenciesData.value.first(item::equals).isSelected = !item.isSelected
    }

}

class EmptyDataError : Exception()