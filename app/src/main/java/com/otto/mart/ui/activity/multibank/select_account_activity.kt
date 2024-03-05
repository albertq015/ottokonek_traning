package com.otto.mart.ui.activity.multibank

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.api.OttoKonekAPI
import com.otto.mart.model.APIModel.Response.multibank.AccountListResponse
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.ApiRoseCallback
import com.otto.mart.ui.Partials.adapter.multibank.SelectAccountBankAdapter
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.transaction.withdraw.WithdrawActivity.Companion.KEY_BANK_DATA_LIST
import kotlinx.android.synthetic.main.activity_list_multibank.*
import kotlinx.android.synthetic.main.toolbar.*

open class SelectAccountMultiBankActivity : AppActivity() {

    var mSelectedBank: AccountListResponse? = null
    val bankList = mutableListOf<AccountListResponse>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_multibank)

        init()
        displayListData(bankList)
    }

    fun init() {
        tvToolbarTitle.text = "Select Account"

        btnToolbarBack.setOnClickListener {

            setResult(RESULT_CANCELED)
            finish()
        }
        btnSelected.setOnClickListener {
            if (mSelectedBank == null) {
                Toast.makeText(this, "Select Account!", Toast.LENGTH_SHORT).show()

            }else{
                val intentResult = Intent().apply {
                    putExtra(KEY_BANK_DATA_LIST, Gson().toJson(mSelectedBank))
                }
                setResult(RESULT_OK, intentResult)
                finish()
            }
        }

        callListAccounBank()
    }

    open fun callListAccounBank() {

        OttoKonekAPI.getListAccountBankWithBin(this, SessionManager.getPrefLogin().bin, object :
            ApiRoseCallback<AccountListResponse>() {
            override fun onResponseSuccess(body: AccountListResponse?) {
                bankList.add(body!!)
                displayListData(bankList)
            }

            override fun onApiServiceFailed(throwable: Throwable?) {

            }

        })

    }

    fun onBankSelected(bank: AccountListResponse) {
        mSelectedBank = bank

        btnSelected.isEnabled = true
        btnSelected.background = ContextCompat.getDrawable(this, R.drawable.button_primary_selector)
    }


    open fun displayListData(dataAccount: MutableList<AccountListResponse>) {
        reva.layoutManager = LinearLayoutManager(this)


//        recycler_view.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
//            this,
//            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
//            false
//        )

        val adapter = SelectAccountBankAdapter().apply {
            listener = ::onBankSelected
            // selectedItemId = sel
        }
        reva.adapter = adapter
        adapter.mDataSet = dataAccount


        //        recycler_view.adapter = adapter
    }
}