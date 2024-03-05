package com.otto.mart.ui.activity.paymentMethod

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.otto.mart.R
import com.otto.mart.api.OttoKonekAPI
import com.otto.mart.model.APIModel.Response.multibank.AccountListResponse
import com.otto.mart.support.util.ApiRoseCallback
import com.otto.mart.ui.Partials.adapter.multibank.LinkedAccountAdapter2
import kotlinx.android.synthetic.main.activity_linked_account.*
import androidx.lifecycle.Observer
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.multibank.*
import kotlinx.android.synthetic.main.activity_linked_account.rv_list
import kotlinx.android.synthetic.main.dialog_select_payment.*
import kotlinx.android.synthetic.main.toolbar.*

class PaymentSelectType  : AppActivity() {

    val presenter: ListLinkedBankAccountPresenter = ListLinkedBankAccountPresenterImpl(this)
    var accountNumber = ""
    private var label = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_select_payment)

        label = intent.getStringExtra("label").toString()

        init()
    }

    fun init() {
        tvToolbarTitle.text = "Select Payment Type"
        imgToolbarLeft.setOnClickListener {
            onBackPressed()
        }

        if (label.equals("pad")){
            layout_header.visibility = View.VISIBLE
        }


        callListAccounBank()


    }

    fun callListAccounBank() {
        showProgressDialog(R.string.loading_message)
        OttoKonekAPI.getListAccountBank(this, object :
            ApiRoseCallback<AccountListResponse>() {

            override fun onResponseSuccess(body: AccountListResponse?) {
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
