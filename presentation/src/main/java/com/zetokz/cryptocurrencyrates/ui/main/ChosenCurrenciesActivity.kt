package com.zetokz.cryptocurrencyrates.ui.main

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import bindView
import com.kennyc.view.MultiStateView
import com.zetokz.cryptocurrencyrates.R
import com.zetokz.cryptocurrencyrates.base.BaseActivity
import com.zetokz.cryptocurrencyrates.ui.addcurrency.AddCurrencyActivity
import com.zetokz.cryptocurrencyrates.ui.main.adapter.ChosenCurrenciesAdapter
import com.zetokz.cryptocurrencyrates.ui.model.CurrencyItem
import com.zetokz.cryptocurrencyrates.util.extension.getViewModel
import com.zetokz.cryptocurrencyrates.util.extension.showContentState
import com.zetokz.cryptocurrencyrates.util.extension.showEmptyState
import com.zetokz.cryptocurrencyrates.util.extension.showErrorState
import com.zetokz.cryptocurrencyrates.util.extension.subscribeNoError
import com.zetokz.cryptocurrencyrates.util.list.SpacingItemDecoration
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
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
        println("Not implemented") /*TODO("not implemented")*/
    }

    private fun observeData() {
        viewModel.currenciesData
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { if (it.isEmpty()) multiStateView.showEmptyState() else multiStateView.showContentState() }
            .doOnError { multiStateView.showErrorState() }
            .subscribeNoError(currencyRatesAdapter::dispatchNewItems)
            .addTo(disposables)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
    }

    private fun initAdapter() {
        currencyRatesAdapter = ChosenCurrenciesAdapter(onCurrencyClickedAction = viewModel.currencyItemClick::onNext)
    }

    private fun initViews() {
        listCurrencyRates.apply {
            adapter = currencyRatesAdapter
            addItemDecoration(SpacingItemDecoration.create {
                spaceSize = context.resources.getDimensionPixelSize(R.dimen.small)
                showBetween = true
            })
        }

        buttonAdd.setOnClickListener { viewModel.addClick.onNext(true) }
    }
}