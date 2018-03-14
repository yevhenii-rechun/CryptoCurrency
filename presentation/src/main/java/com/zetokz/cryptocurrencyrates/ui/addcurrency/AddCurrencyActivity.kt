package com.zetokz.cryptocurrencyrates.ui.addcurrency

import android.app.AlertDialog
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.widget.Button
import android.widget.SearchView
import bindView
import com.kennyc.view.MultiStateView
import com.zetokz.cryptocurrencyrates.R
import com.zetokz.cryptocurrencyrates.base.BaseActivity
import com.zetokz.cryptocurrencyrates.ui.addcurrency.adapter.AvailableCurrenciesAdapter
import com.zetokz.cryptocurrencyrates.util.extension.getViewModel
import com.zetokz.cryptocurrencyrates.util.extension.onQueryTextChanged
import com.zetokz.cryptocurrencyrates.util.extension.showContentState
import com.zetokz.cryptocurrencyrates.util.extension.showErrorState
import com.zetokz.cryptocurrencyrates.util.extension.showLoadingState
import com.zetokz.cryptocurrencyrates.util.list.SpacingItemDecoration
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class AddCurrencyActivity : BaseActivity(), AddCurrencyRouter {

    companion object {

        fun getIntent(context: Context) = Intent(context, AddCurrencyActivity::class.java)
    }

    private val toolbar: Toolbar by bindView(R.id.toolbar)
    private val listCurrencyRates: RecyclerView by bindView(R.id.list_chosen_currency)
    private val multiStateView: MultiStateView by bindView(R.id.multi_state_view)
    private val buttonSave: FloatingActionButton by bindView(R.id.button_save)
    private val buttonRefresh: Button by bindView(R.id.button_refresh)

    @Inject lateinit var factory: ViewModelProvider.Factory

    private lateinit var viewModel: AddCurrencyViewModel
    private lateinit var currencyRatesAdapter: AvailableCurrenciesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_currency)
        viewModel = getViewModel<AddCurrencyViewModel>(factory)

        initAdapter()
        initToolbar()
        initViews()

        observeData()
    }

    override fun close() {
        finish()
    }

    override fun showCloseDialog() {
        AlertDialog.Builder(this)
            .setMessage(R.string.activity_choose_currency_dialog_close_without_saving_message)
            .setPositiveButton(R.string.action_save, { _, _ -> viewModel.saveCurrenciesClick.accept(true) })
            .setNegativeButton(R.string.action_exit, { _, _ -> viewModel.saveCurrenciesClick.accept(false) })
            .show()
    }

    override fun showProgress() {
        showBlockingProgress()
    }

    override fun hideProgress() {
        hideBlockingProgress()
    }

    override fun onBackPressed() {
        viewModel.backButtonClick.accept(true)
    }

    @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
    private fun observeData() {
        disposables += viewModel.currenciesData
            .subscribeOn(Schedulers.io())
            .map { it.map { it.copy() } }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = currencyRatesAdapter::dispatchNewItems)

        disposables += viewModel.viewState
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = { currencyViewState ->
                when (currencyViewState) {
                    AddCurrencyState.PROGRESS -> multiStateView.showLoadingState()
                    AddCurrencyState.CONTENT -> multiStateView.showContentState()
                    AddCurrencyState.ERROR -> multiStateView.showErrorState()
                }
            })
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_home_indicator)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchView = menu?.findItem(R.id.item_search)?.actionView as SearchView
        searchView.queryHint = getString(R.string.activity_choose_currency_search_hint)
        searchView.onQueryTextChanged({ newQuery -> newQuery?.let(viewModel.filterCurrencyName::accept) })
        searchView.isIconified = true
        return true
    }

    private fun initAdapter() {
        currencyRatesAdapter = AvailableCurrenciesAdapter(viewModel.currencyItemSelect::accept)
    }

    private fun initViews() {
        listCurrencyRates.apply {
            adapter = currencyRatesAdapter
            addItemDecoration(SpacingItemDecoration.create {
                spaceSize = context.resources.getDimensionPixelSize(R.dimen.small)
                showBetween = true
            })
        }

        buttonSave.setOnClickListener { viewModel.saveCurrenciesClick.accept(true) }
        buttonRefresh.setOnClickListener { viewModel.refreshDataClick.accept(true) }
    }

}
