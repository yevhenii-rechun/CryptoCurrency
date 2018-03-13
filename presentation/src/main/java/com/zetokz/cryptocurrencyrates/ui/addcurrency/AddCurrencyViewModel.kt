package com.zetokz.cryptocurrencyrates.ui.addcurrency

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.zetokz.cryptocurrencyrates.base.BaseViewModel
import com.zetokz.cryptocurrencyrates.ui.model.CurrencyItemSelectable
import com.zetokz.cryptocurrencyrates.ui.model.toCurrencySelectableItems
import com.zetokz.cryptocurrencyrates.util.extension.hasElement
import com.zetokz.cryptocurrencyrates.util.extension.minusAssign
import com.zetokz.data.interactor.CurrencyRatesInteractor
import com.zetokz.data.model.Currency
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposables
import io.reactivex.rxkotlin.Singles
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Yevhenii Rechun on 3/10/18.
 * Copyright © 2017. All rights reserved.
 */
class AddCurrencyViewModel @Inject constructor(
    private val addCurrencyRouter: AddCurrencyRouter,
    private val currencyRatesInteractor: CurrencyRatesInteractor
) : BaseViewModel() {

    val currenciesData: BehaviorRelay<List<CurrencyItemSelectable>> = BehaviorRelay.create()
    val currencyItemSelect: PublishRelay<CurrencyItemSelectable> = PublishRelay.create()

    val viewState: BehaviorRelay<AddCurrencyState> = BehaviorRelay.create()

    val saveCurrenciesClick: PublishRelay<Boolean> = PublishRelay.create()
    val backButtonClick: PublishRelay<Boolean> = PublishRelay.create()
    val refreshDataClick: PublishRelay<Boolean> = PublishRelay.create()

    val filterCurrencyName: BehaviorRelay<String> = BehaviorRelay.create()

    private lateinit var rawCurrency: List<Currency>
    private var needToShowSaveChangesDialog: Boolean = false
    private val currenciesToSave = mutableListOf<Currency>()

    private var loadCurrenciesDisposable = Disposables.empty()

    init {
        loadCurrencies()

        disposables += currenciesData
            .subscribeOn(Schedulers.io())
            .subscribe({ viewState.accept(AddCurrencyState.CONTENT) }, ::handleCommonError)

        disposables += currencyItemSelect
            .subscribeOn(Schedulers.io())
            .doOnNext(::saveAndChangeSelection)
            .map { currenciesData.value }
            .subscribe(currenciesData::accept, ::handleCommonError)

        disposables += saveCurrenciesClick
            .subscribeOn(Schedulers.io())
            .flatMapCompletable { saveAndClose(it) }
            .subscribeBy(::handleCommonError)

        disposables += backButtonClick
            .subscribeOn(Schedulers.io())
            .map { needToShowSaveChangesDialog }
            .doOnNext { isNeedToShowDialog ->
                if (isNeedToShowDialog) addCurrencyRouter.showCloseDialog()
                else addCurrencyRouter.close()
            }
            .subscribeBy(::handleCommonError)

        disposables += refreshDataClick
            .subscribeOn(Schedulers.io())
            .doOnNext { loadCurrencies() }
            .subscribeBy(::handleCommonError)
    }

    private fun saveAndClose(isNeedToSave: Boolean): Completable =
        (if (isNeedToSave) currencyRatesInteractor.saveCurrencies(currenciesToSave)
        else Completable.complete())
            .doOnComplete(addCurrencyRouter::close)

    private fun findAndApplySelection(
        needToFindSelection: List<CurrencyItemSelectable>,
        selectedItems: List<Currency>
    ): Single<List<CurrencyItemSelectable>> = Observable.fromIterable(needToFindSelection)
        .map { item -> item.apply { isSelected = selectedItems.hasElement { it.name == item.shortName } } }
        .toList()

    private fun loadCurrencies() {
        disposables -= loadCurrenciesDisposable
        loadCurrenciesDisposable = Singles.zip(
            currencyRatesInteractor.getAvailableCurrencies()
                .doOnSuccess { rawCurrency = it }
                .map { it.toCurrencySelectableItems() },
            currencyRatesInteractor.getChosenCurrencies()
                .first(listOf())
                .doOnSuccess { currenciesToSave.addAll(it) }
        )
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { viewState.accept(AddCurrencyState.PROGRESS) }
            .doOnSuccess { (remoteCurrencies) -> if (remoteCurrencies.isEmpty()) throw EmptyDataError() }
            .flatMap { (allCurrencies, savedCurrencies) -> findAndApplySelection(allCurrencies, savedCurrencies) }
            .doOnSuccess { observeSearchFilter() }
            .subscribe(currenciesData::accept, { viewState.accept(AddCurrencyState.ERROR) })
        disposables += loadCurrenciesDisposable
    }

    private fun observeSearchFilter() {
        disposables += filterCurrencyName
            .subscribeOn(Schedulers.io())
            .map { query -> rawCurrency.filter { it.name.contains(query, ignoreCase = true) } }
            .map { it.toCurrencySelectableItems() }
            .concatMap { findAndApplySelection(it, currenciesToSave).toObservable() }
            .subscribe(currenciesData::accept, ::handleCommonError)
    }

    private fun saveAndChangeSelection(item: CurrencyItemSelectable) {
        needToShowSaveChangesDialog = true
        if (item.isSelected) removeSelection(item) else applySelection(item)
    }

    private fun applySelection(item: CurrencyItemSelectable) {
        rawCurrency.firstOrNull { it.name == item.shortName }?.let(currenciesToSave::add)
        currenciesData.value.first(item::equals).isSelected = true
    }

    private fun removeSelection(item: CurrencyItemSelectable) {
        currenciesToSave.firstOrNull { it.name == item.shortName }?.let(currenciesToSave::remove)
        currenciesData.value.first(item::equals).isSelected = false
    }
}

enum class AddCurrencyState {
    PROGRESS, CONTENT, ERROR
}

class EmptyDataError : Exception()