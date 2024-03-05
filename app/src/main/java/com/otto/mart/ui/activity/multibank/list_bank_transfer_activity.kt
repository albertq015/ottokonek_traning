package com.otto.mart.ui.activity.multibank

import android.content.Intent
import android.os.Bundle
import com.otto.mart.R
import com.otto.mart.api.OttoKonekAPI
import com.otto.mart.model.APIModel.Response.multibank.BankTransferListResponse
import com.otto.mart.model.localmodel.profile.ProfileMenu
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.ApiCallback
import com.otto.mart.ui.Partials.adapter.multibank.ListBankAccountAdapter
import com.otto.mart.ui.activity.AppActivity
import kotlinx.android.synthetic.main.activity_linked_account.*
import kotlinx.android.synthetic.main.toolbar.*

class ListBankTransferActivity : AppActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_linked_account)
        init()


    }

    fun init() {
        tvToolbarTitle.text = "List Bank Account"
        imgToolbarLeft.setOnClickListener {
            onBackPressed()
        }
        btn_add_account.setOnClickListener {
//            var intent = Intent(this, AddBankAccountActivity::class.java)
//            startActivity(intent)
            comingSoonDialogBase()
        }
        tv_name_bank.text = "Add Bank Account"
        getBankTransferList()
    }

    fun listData(dataAccount: MutableList<BankTransferListResponse.Data>) {
        rv_list.setHasFixedSize(true)
        rv_list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            this,
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )
        val adapter = ListBankAccountAdapter(dataAccount)
        rv_list.adapter = adapter
    }


    private fun menuSelected(data: ProfileMenu, position: Int) {

        if (data.intent != null) {
            startActivity(data.intent)
        }


    }



    private fun getBankTransferList() {
        showProgressDialog(R.string.loading_message)
        OttoKonekAPI.getTransferBankList(
            this,

            object : ApiCallback<BankTransferListResponse?>() {
                override fun onResponseSuccess(body: BankTransferListResponse?) {
                    dismissProgressDialog()
                    listData(body!!.data)
                }

                override fun onApiServiceFailed(throwable: Throwable?) {
                    dismissProgressDialog()
                }

            })
    }
}