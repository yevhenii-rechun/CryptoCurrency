package com.zetokz.cryptocurrencyrates.ui.exchangerate

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.view.setPadding
import bindView
import com.kennyc.view.MultiStateView
import com.zetokz.cryptocurrencyrates.R
import com.zetokz.cryptocurrencyrates.base.BaseActivity
import com.zetokz.cryptocurrencyrates.ui.model.ConversionItem
import com.zetokz.cryptocurrencyrates.ui.model.CurrencyItem
import com.zetokz.cryptocurrencyrates.util.extension.getViewModel
import com.zetokz.cryptocurrencyrates.util.extension.loadImage
import com.zetokz.cryptocurrencyrates.util.extension.showContentState
import com.zetokz.cryptocurrencyrates.util.extension.showErrorState
import com.zetokz.cryptocurrencyrates.util.extension.showLoadingState
import com.zetokz.cryptocurrencyrates.util.extension.toSpannedHtml
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.zipWith
import javax.inject.Inject

class ExchangeRateActivity : BaseActivity() {

    companion object {

        private const val EXTRA_CURRENCY_ITEM = "EXTRA_CURRENCY_ITEM"

        fun getIntent(context: Context, currencyItem: CurrencyItem) = Intent(context, ExchangeRateActivity::class.java)
            .apply { putExtra(EXTRA_CURRENCY_ITEM, currencyItem) }
    }

    internal val currencyItem: CurrencyItem get() = intent.getParcelableExtra(EXTRA_CURRENCY_ITEM)

    //todo add swipe to refresh
    private val toolbar: Toolbar by bindView(R.id.toolbar)
    private val iconCryptoLogo: ImageView by bindView(R.id.icon_crypto_logo)
    private val textCurrencyNameShort: TextView by bindView(R.id.text_currency_name_short)
    private val textCurrencyNameFull: TextView by bindView(R.id.text_currency_name_full)
    private val containerConversions: LinearLayout by bindView(R.id.container_conversions)
    private val multiStateView: MultiStateView by bindView(R.id.multi_state_view)
    private val buttonChooseAnother: Button by bindView(R.id.button_choose_another)

    @Inject lateinit var factory: ViewModelProvider.Factory

    private lateinit var viewModel: ExchangeRateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exchange_rate)
        viewModel = getViewModel<ExchangeRateViewModel>(factory)

        initToolbar()
        initViews()

        observeData()
    }

    @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
    private fun observeData() {
        disposables += viewModel.conversionData
            .observeOn(AndroidSchedulers.mainThread())
            .zipWith(viewModel.headerData.toObservable())
            .subscribeBy(onNext = { (conversions, currencyItem) -> renderConversions(currencyItem, conversions) })

        disposables += viewModel.viewState
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = { currencyViewState ->
                when (currencyViewState) {
                    ExchangeRateState.LOADING -> multiStateView.showLoadingState()
                    ExchangeRateState.CONTENT -> multiStateView.showContentState()
                    ExchangeRateState.ERROR -> multiStateView.showErrorState()
                }
            })
    }

    private fun renderConversions(currencyItem: CurrencyItem, conversions: List<ConversionItem>) {
        with(currencyItem) {
            iconLink?.let(iconCryptoLogo::loadImage)
            textCurrencyNameShort.text = shortName
            textCurrencyNameFull.text = fullName
        }

        clearExchangeRates()
        addExchangeRates(conversions)
    }

    private fun addExchangeRates(conversions: List<ConversionItem>) {
        val conversionViewTag = getString(R.string.conversion_view_tag)
        conversions.forEach {
            containerConversions.addView(
                TextView(this).apply {
                    tag = conversionViewTag
                    text = getString(
                        R.string.activity_exchange_base_currency_to_conversion,
                        it.baseCurrency, it.conversionCurrencyValue.toString(), it.conversionCurrencyName
                    ).toSpannedHtml()
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).also { setPadding(resources.getDimensionPixelSize(R.dimen.normal)) }
                }
            )
        }
    }

    private fun clearExchangeRates() {
        val conversionViewTag = getString(R.string.conversion_view_tag)
        var exchangeRateView = containerConversions.findViewWithTag<TextView>(conversionViewTag)
        while (exchangeRateView != null) {
            containerConversions.removeView(exchangeRateView)
            exchangeRateView = containerConversions.findViewWithTag<TextView>(conversionViewTag)
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_home_indicator)
    }

    private fun initViews() {
        buttonChooseAnother.setOnClickListener { finish() }
    }
}
