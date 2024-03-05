package com.otto.mart.ui.activity.cashOut

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.api.OttoKonekAPI
import com.otto.mart.model.APIModel.Response.multibank.AccountListResponse
import com.otto.mart.model.APIModel.Response.multibank.BankTransferListResponse
import com.otto.mart.model.APIModel.Response.multibank.PartnerBankListResponse
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.ApiCallback
import com.otto.mart.support.util.ApiRoseCallback
import com.otto.mart.support.util.px
import com.otto.mart.ui.Partials.adapter.multibank.BankSelectionMultiBankAdapter
import com.otto.mart.ui.Partials.adapter.multibank.TransferSelectedListAdapter
import com.otto.mart.ui.Partials.decorator.SpaceDecorator
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.cashOut.CashOutSelectAmountActivity.Companion.KEY_BANK_DATA
import com.otto.mart.ui.activity.multibank.TransferMenuAccountActivity
import com.otto.mart.ui.activity.profile.BankSelectionActivity
import kotlinx.android.synthetic.main.content_cash_out_bank_list.btnNext
import kotlinx.android.synthetic.main.content_setting.recyclerview
import kotlinx.android.synthetic.main.toolbar.*


class CashOutBankListActivity : AppActivity() {

    var KEY_API_GET_BANK_LIST = 100
    var transfer = ""
    var bin = ""
    lateinit var mSelectedBank: PartnerBankListResponse.Data

    lateinit var mSelectedBankTransfer: BankTransferListResponse.Data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_out_bank_list)

        initView()
        initRecyclerview()
        transfer = intent?.getStringExtra("name").toString()
        bin = intent?.getStringExtra("bin").toString()

        if (transfer.isEmpty()) {
            callListAccounBank()
        } else {
            getBankTransferList(bin)
        }

    }

    private fun initView() {
        tvToolbarTitle.text = getString(R.string.title_activity_cash_out_bank_list)

        btnNext.isEnabled = false
        btnNext.background =
            ContextCompat.getDrawable(this, R.drawable.button_white_grey_selected_bg)

        imgToolbarLeft.setOnClickListener {
            onBackPressed()
        }

        btnNext.setOnClickListener {
            if (transfer.isEmpty()) {
                val intentResult = Intent().apply {
                    putExtra(KEY_BANK_DATA, Gson().toJson(mSelectedBank))
                }

                setResult(TransferMenuAccountActivity.KEY_REQUEST_LINK_TO, intentResult)
                finish()
            } else {
                val intentResult = Intent().apply {
                    putExtra(KEY_BANK_DATA, Gson().toJson(mSelectedBankTransfer))
                }
                setResult(RESULT_OK, intentResult)
                finish()
            }
        }
    }

    private fun initRecyclerview() {
        recyclerview.layoutManager = LinearLayoutManager(this)
    }

    private fun displayBankList(bank: MutableList<PartnerBankListResponse.Data>) {
        val selectedId = intent.getIntExtra(BankSelectionActivity.SELECTED_ID, -1)
        var adapter = BankSelectionMultiBankAdapter(bank).apply {
            listener = ::onBankSelected
            selectedItemId = selectedId
        }

        recyclerview.adapter = adapter
        adapter.banks = bank
    }

    private fun displayBankTransferList(bank: MutableList<BankTransferListResponse.Data>) {
        val selectedId = intent.getIntExtra(BankSelectionActivity.SELECTED_ID, -1)
        var adapter = TransferSelectedListAdapter(bank).apply {
            listener = ::onBankSelectedTransfer
            selectedItemId = selectedId
        }

        recyclerview.adapter = adapter
        adapter.banks = bank
    }


    private fun onBankSelectedTransfer(bank: BankTransferListResponse.Data) {
        mSelectedBankTransfer = bank

        btnNext.isEnabled = true
        btnNext.background = ContextCompat.getDrawable(this, R.drawable.button_primary_selector)
    }

    private fun onBankSelected(bank: PartnerBankListResponse.Data) {
        mSelectedBank = bank

        btnNext.isEnabled = true
        btnNext.background = ContextCompat.getDrawable(this, R.drawable.button_primary_selector)
    }

    fun callListAccounBank() {

        OttoKonekAPI.getListBankPartner(this, object : ApiCallback<PartnerBankListResponse?>() {


            override fun onApiServiceFailed(throwable: Throwable) {
                dismissProgressDialog()
                onApiResponseError()

            }

            override fun onResponseSuccess(body: PartnerBankListResponse?) {
                dismissProgressDialog()
                body?.let {
                    displayBankList(it.data)
                }
            }
        });
    }


    private fun getBankTransferList(bin: String) {
        showProgressDialog(R.string.loading_message)
        OttoKonekAPI.getTransferBankListMid(
            bin,
            this,
            object : ApiCallback<BankTransferListResponse?>() {
                override fun onResponseSuccess(body: BankTransferListResponse?) {
                    dismissProgressDialog()
                    body?.let {
                        displayBankTransferList(it.data)
                    }
                }

                override fun onApiServiceFailed(throwable: Throwable?) {
                    dismissProgressDialog()
                }

            })
    }
}