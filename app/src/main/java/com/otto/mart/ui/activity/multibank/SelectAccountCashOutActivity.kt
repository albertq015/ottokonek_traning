package com.otto.mart.ui.activity.multibank

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.otto.mart.R
import com.otto.mart.api.OttoKonekAPI
import com.otto.mart.model.APIModel.Response.multibank.AccountListResponse
import com.otto.mart.support.util.ApiRoseCallback
import com.otto.mart.ui.Partials.adapter.multibank.SelectAccountCashOutAdapter
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.profile.BankSelectionActivity
import com.otto.mart.ui.activity.transaction.withdraw.WithdrawActivity
import kotlinx.android.synthetic.main.content_cash_out_bank_list.*
import kotlinx.android.synthetic.main.toolbar.*

class SelectAccountCashOutActivity : AppActivity() {
    lateinit var mSelectedBank: AccountListResponse.Data
    val presenter: ListLinkedBankAccountPresenter = ListLinkedBankAccountPresenterImpl(this)
    var selectedPos = -1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_out_bank_list)

        initView()
        initRecyclerview()
        callListAccounBank()
    }

    private fun initView() {
        tvToolbarTitle.text = getString(R.string.title_activity_cash_out_bank_list_2)

        btnNext.isEnabled = false
        btnNext.background =
            ContextCompat.getDrawable(this, R.drawable.button_white_grey_selected_bg)

        imgToolbarLeft.setOnClickListener {
            onBackPressed()
        }

        btnNext.setOnClickListener {

            val intentResult = Intent().apply {
                putExtra(WithdrawActivity.KEY_BA_POS, selectedPos)
            }
            setResult(RESULT_OK, intentResult)
            finish()


//            if (mSelectedBank != null) {
//                val intentResult = Intent().apply {
//                    putExtra(
//                        TransferMenuAccountActivity.KEY_LINK_DATA_TO,
//                        Gson().toJson(mSelectedBank)
//                    )
//                }
//
//                setResult(TransferMenuAccountActivity.KEY_REQUEST_BANK_TO, intentResult)
//                finish()
//            }
        }
    }

    private fun initRecyclerview() {
        recyclerview.layoutManager = LinearLayoutManager(this)
    }

    private fun displayBankList(bank: MutableList<AccountListResponse.Data>) {
        val selectedId = intent.getIntExtra(BankSelectionActivity.SELECTED_ID, -1)
        presenter.setDataAccount(bank)
        var adapter = SelectAccountCashOutAdapter(presenter.getDataAccount().value!!).apply {
            listener = ::onBankSelected
        }

        presenter.getDataAccount().observe(this,
            Observer<MutableList<LLBAViewModel>> {
                recyclerview.adapter?.notifyDataSetChanged()
            })

        recyclerview.adapter = adapter

    }

    private fun onBankSelected(pos : Int) {
        selectedPos = pos

        btnNext.isEnabled = true
        btnNext.background = ContextCompat.getDrawable(this, R.drawable.button_primary_selector)
    }

    fun callListAccounBank() {
        OttoKonekAPI.getListAccountBank(this, object :
            ApiRoseCallback<AccountListResponse>() {
            override fun onResponseSuccess(body: AccountListResponse?) {
                presenter.setDataAccount(body?.data!!)
                presenter.loadIssuerBalance()
                displayBankList(body!!.data)
            }


            override fun onApiServiceFailed(throwable: Throwable?) {

            }

        })

    }
}