package com.zetokz.cryptocurrencyrates.ui.addcurrency

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import bindView
import com.kennyc.view.MultiStateView
import com.zetokz.cryptocurrencyrates.R
import com.zetokz.cryptocurrencyrates.base.BaseActivity
import com.zetokz.cryptocurrencyrates.ui.addcurrency.adapter.AvailableCurrenciesAdapter
import com.zetokz.cryptocurrencyrates.util.extension.getViewModel
import com.zetokz.cryptocurrencyrates.util.extension.showContentState
import com.zetokz.cryptocurrencyrates.util.extension.showErrorState
import com.zetokz.cryptocurrencyrates.util.extension.subscribeNoError
import com.zetokz.cryptocurrencyrates.util.list.SpacingItemDecoration
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
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
            .setPositiveButton(R.string.action_save, { _, _ -> viewModel.saveCurrencies.onNext(true) })
            .setNegativeButton(R.string.action_exig, { _, _ -> viewModel.saveCurrencies.onNext(false) })
            .show()
    }

    override fun showProgress() {
        showBlockingProgress()
    }

    override fun hideProgress() {
        hideBlockingProgress()
    }

    override fun onBackPressed() {
        viewModel.clickBack.onNext(true)
    }

    private fun observeData() {
        viewModel.currenciesData
            .subscribeOn(Schedulers.io())
            .map { it.map { it.copy() } }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { multiStateView.showContentState() }
            .doOnError { multiStateView.showErrorState() }
            .subscribeNoError(currencyRatesAdapter::dispatchNewItems)
            .addTo(disposables)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initAdapter() {
        currencyRatesAdapter =
                AvailableCurrenciesAdapter { viewModel.currencyItemSelect.onNext(it) }
    }

    private fun initViews() {
        listCurrencyRates.apply {
            adapter = currencyRatesAdapter
            addItemDecoration(SpacingItemDecoration.create {
                spaceSize = context.resources.getDimensionPixelSize(R.dimen.small)
                showBetween = true
            })
        }

        buttonSave.setOnClickListener { viewModel.saveCurrencies.onNext(true) }
    }

}
