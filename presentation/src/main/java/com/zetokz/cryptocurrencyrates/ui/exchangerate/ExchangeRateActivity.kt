package com.zetokz.cryptocurrencyrates.ui.exchangerate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import bindView
import com.kennyc.view.MultiStateView
import com.zetokz.cryptocurrencyrates.R

class ExchangeRateActivity : AppCompatActivity() {

    private val toolbar: Toolbar by bindView(R.id.toolbar)
    private val multiStateView: MultiStateView by bindView(R.id.multi_state_view)

    companion object {

        fun getIntent(context: Context) = Intent(context, ExchangeRateActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exchange_rate)

        initToolbar()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_home_indicator)
    }

}
