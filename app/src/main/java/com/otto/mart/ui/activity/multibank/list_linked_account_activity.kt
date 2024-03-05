package com.otto.mart.ui.activity.multibank

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.otto.mart.R
import com.otto.mart.api.OttoKonekAPI
import com.otto.mart.model.APIModel.Response.multibank.AccountListResponse
import com.otto.mart.support.util.ApiRoseCallback
import com.otto.mart.ui.Partials.adapter.multibank.LinkedAccountAdapter2
import com.otto.mart.ui.activity.AppActivity
import kotlinx.android.synthetic.main.activity_linked_account.*
import kotlinx.android.synthetic.main.toolbar.*

class ListLinkedBankAccountActivity : AppActivity() {

    val presenter: ListLinkedBankAccountPresenter = ListLinkedBankAccountPresenterImpl(this)
    var accountNumber = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_linked_account)
        init()
        //listData()
    }

    fun init() {
        tvToolbarTitle.text = "Linked Account(s)"
        imgToolbarLeft.setOnClickListener {
            onBackPressed()
        }
        btn_add_account.setOnClickListener {
//            val intent = Intent(this, AddLinkAccountActivity::class.java)
//            startActivity(intent)
            comingSoonDialogBase()
        }
        tv_name_bank.text = "Link New Account"
        callListAccounBank()
        //   callListBankartner()

    }

    fun callListAccounBank() {
        showProgressDialog(R.string.loading_message)
        OttoKonekAPI.getListAccountBank(this, object :
            ApiRoseCallback<AccountListResponse>() {

            override fun onResponseSuccess(body: AccountListResponse?) {
//                listData(body!!.data)
                dismissProgressDialog()
                presenter.setDataAccount(body?.data!!)
                listDataMVVM()
                presenter.loadIssuerBalance()
            }

            override fun onApiServiceFailed(throwable: Throwable?) {
                dismissProgressDialog()
                showErrorDialog("Failed Network")
            }

        })

    }

//    fun listData(dataAccount: MutableList<AccountListResponse.Data>) {
//        rv_list.setHasFixedSize(true)
//        rv_list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
//            this,
//            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
//            false
//        )
//
//        val adapter = LinkedAccountAdapter(dataAccount)
//        rv_list.adapter = adapter
//        adapter.setmOnViewClickListener(object : LinkedAccountAdapter.OnViewClickListener {
//            override fun onViewClickListener(position: Int) {
//                menuSelected(position)
//
//            }
//        })
//    }

    @SuppressLint("NotifyDataSetChanged")
    fun listDataMVVM() {
        presenter.getDataAccount().observe(this,
            Observer<MutableList<LLBAViewModel>> {
                rv_list.adapter?.notifyDataSetChanged()
            })

        rv_list.setHasFixedSize(true)
        rv_list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            this,
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )

        val adapter = LinkedAccountAdapter2(presenter.getDataAccount().value!!)
        rv_list.adapter = adapter
        adapter.setmOnViewClickListener(object : LinkedAccountAdapter2.OnViewClickListener {

            override fun onViewClickListener(
                position: Int,
                accountNumber: String,
                responCode: Int?
            ) {
                if (responCode != 498) {
                    menuSelected(position, accountNumber)
                }
            }
        })
        adapter.setOnRetryRequestListener(object : LinkedAccountAdapter2.OnRetryViewCLickListener {
            override fun onRetryViewClickListener(pos: Int) {
                presenter.onRequestRetry(pos)
            }

        })
    }

    private fun menuSelected(position: Int, accountNumber: String) {
        val intent = Intent(this, DetailAccountActivity::class.java)
        intent.putExtra("pos", position)
        intent.putExtra("accountNumber", accountNumber)
        startActivity(intent)
    }
}


