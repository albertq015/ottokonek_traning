package com.otto.mart.ui.activity.multibank

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.api.OttoKonekAPI
import com.otto.mart.model.APIModel.Response.multibank.AccountListResponse
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.ApiRoseCallback
import com.otto.mart.ui.Partials.adapter.multibank.BankSelectionAccountListAdapter
import com.otto.mart.ui.Partials.adapter.multibank.SelectAccountBankAdapter
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.profile.BankSelectionActivity
import com.otto.mart.ui.activity.qr.payment.QrPaymentOttoKonekActivity
import com.otto.mart.ui.activity.transaction.withdraw.WithdrawActivity
import kotlinx.android.synthetic.main.activity_linked_account.*
import kotlinx.android.synthetic.main.activity_list_multibank.*
import kotlinx.android.synthetic.main.content_cash_out_bank_list.*
import kotlinx.android.synthetic.main.toolbar.*

class SelectAccountQrActivity : AppActivity() {

    lateinit var mSelectedBank: AccountListResponse.Data
    var binFromList: String? = null
    val presenter: ListLinkedBankAccountPresenter = ListLinkedBankAccountPresenterImpl(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_out_bank_list)


        binFromList = intent?.getStringExtra("binFromList").toString()

        initView()
        initRecyclerview()
        callListAccounBank()
    }

    private fun initView() {
        tvToolbarTitle.text = getString(R.string.text_select_account)

        btnNext.isEnabled = false
        btnNext.background =
            ContextCompat.getDrawable(this, R.drawable.button_white_grey_selected_bg)

        imgToolbarLeft.setOnClickListener {
            onBackPressed()
        }

        btnNext.setOnClickListener {
//            if (mSelectedBank != null) {
//                val intentResult = Intent().apply {
//                    putExtra(
//                        QrPaymentOttoKonekActivity.KEY_BANK_DATA_LIST,
//                        Gson().toJson(mSelectedBank)
//                    )
//                }
//
//                setResult(RESULT_OK, intentResult)
//                finish()
//            }

            if (mSelectedBank == null) {
                Toast.makeText(this, "Select Account!", Toast.LENGTH_SHORT).show()

            } else {
                val intentResult = Intent().apply {
                    putExtra(
                        QrPaymentOttoKonekActivity.KEY_BANK_DATA_LIST,
                        Gson().toJson(mSelectedBank)
                    )
                }
                setResult(AppActivity.RESULT_OK, intentResult)
                finish()
            }
        }
    }

    private fun initRecyclerview() {
        recyclerview.layoutManager = LinearLayoutManager(this)
    }

    private fun displayBankList(bank: MutableList<AccountListResponse.Data>) {
        val selectedId = intent.getIntExtra(BankSelectionActivity.SELECTED_ID, -1)
        var adapter = BankSelectionAccountListAdapter(presenter.getDataAccount().value!!).apply {
            listener = ::onBankSelected
            selectedItemId = selectedId
        }
        presenter.getDataAccount().observe(this,
            Observer<MutableList<LLBAViewModel>> {
                recyclerview.adapter?.notifyDataSetChanged()
            })

        recyclerview.adapter = adapter
        adapter.banks = bank
    }

    private fun onBankSelected(bank: AccountListResponse.Data) {
        mSelectedBank = bank

        btnNext.isEnabled = true
        btnNext.background = ContextCompat.getDrawable(this, R.drawable.button_primary_selector)
    }

    fun callListAccounBank() {
        showProgressDialog(R.string.loading_message)
        OttoKonekAPI.getListAccountBankWithBin(this, binFromList, object :
            ApiRoseCallback<AccountListResponse>() {
            override fun onResponseSuccess(body: AccountListResponse?) {
                dismissProgressDialog()
                presenter.setDataAccount(body?.data!!)
                presenter.loadIssuerBalance()
                displayBankList(body.data)

            }


            override fun onApiServiceFailed(throwable: Throwable?) {
                dismissProgressDialog()
                showErrorDialog("Failed Network")
            }

        })

    }
}