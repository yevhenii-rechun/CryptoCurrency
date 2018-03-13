package com.zetokz.cryptocurrencyrates.ui.chosencurrencies

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.support.v7.widget.helper.ItemTouchHelper
import bindView
import com.kennyc.view.MultiStateView
import com.zetokz.cryptocurrencyrates.R
import com.zetokz.cryptocurrencyrates.base.BaseActivity
import com.zetokz.cryptocurrencyrates.ui.addcurrency.AddCurrencyActivity
import com.zetokz.cryptocurrencyrates.ui.chosencurrencies.adapter.ChosenCurrenciesAdapter
import com.zetokz.cryptocurrencyrates.ui.exchangerate.ExchangeRateActivity
import com.zetokz.cryptocurrencyrates.ui.model.CurrencyItem
import com.zetokz.cryptocurrencyrates.util.SwipeToDeleteRecyclerCallback
import com.zetokz.cryptocurrencyrates.util.extension.getViewModel
import com.zetokz.cryptocurrencyrates.util.extension.showContentState
import com.zetokz.cryptocurrencyrates.util.extension.showEmptyState
import com.zetokz.cryptocurrencyrates.util.extension.showLoadingState
import com.zetokz.cryptocurrencyrates.util.list.SpacingItemDecoration
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject


class ChosenCurrenciesActivity : BaseActivity(), ChosenCurrenciesRouter {

    private val toolbar: Toolbar by bindView(R.id.toolbar)
    private val listCurrencyRates: RecyclerView by bindView(R.id.list_chosen_currency)
    private val buttonAdd: FloatingActionButton by bindView(R.id.button_add)
    private val multiStateView: MultiStateView by bindView(R.id.multi_state_view)

    @Inject lateinit var factory: ViewModelProvider.Factory

    private lateinit var viewModel: ChosenCurrenciesViewModel
    private lateinit var currencyRatesAdapter: ChosenCurrenciesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chosen_currencies)
        viewModel = getViewModel<ChosenCurrenciesViewModel>(factory)

        initAdapter()
        initToolbar()
        initViews()

        observeData()
    }

    override fun navigateToAddCurrency() {
        startActivity(AddCurrencyActivity.getIntent(this))
    }

    override fun navigateToCurrencyExchangeRates(currencyItem: CurrencyItem) {
        startActivity(ExchangeRateActivity.getIntent(this))
    }

    @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
    private fun observeData() {
        disposables += viewModel.currenciesData
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = currencyRatesAdapter::dispatchNewItems)

        disposables += viewModel.viewState
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = { chosenCurrenciesViewState ->
                when (chosenCurrenciesViewState) {
                    ChosenCurrenciesViewState.LOADING -> multiStateView.showLoadingState()
                    ChosenCurrenciesViewState.CONTENT -> multiStateView.showContentState()
                    ChosenCurrenciesViewState.EMPTY -> multiStateView.showEmptyState()
                }
            })
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
    }

    private fun initAdapter() {
        currencyRatesAdapter = ChosenCurrenciesAdapter(onCurrencyClickedAction = viewModel.currencyItemClick::accept)
    }

    private fun initViews() {
        listCurrencyRates.apply {
            adapter = currencyRatesAdapter
            addItemDecoration(SpacingItemDecoration.create {
                spaceSize = context.resources.getDimensionPixelSize(R.dimen.small)
                showBetween = true
            })
        }
        SwipeToDeleteRecyclerCallback(context = this, onSwipe = {
            viewModel.removeCurrency.accept(currencyRatesAdapter.items[it.adapterPosition] as CurrencyItem)
        }).also { ItemTouchHelper(it).attachToRecyclerView(listCurrencyRates) }

        buttonAdd.setOnClickListener { viewModel.addClick.accept(true) }
    }
}