package com.otto.mart.ui.activity.multibank

import android.annotation.SuppressLint
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.otto.mart.R
import com.otto.mart.model.APIModel.Response.multibank.AccountListResponse
import com.otto.mart.ui.Partials.adapter.multibank.SelectAccountBankAdapter2
import com.otto.mart.ui.activity.cashOut.CashOutSelectAmountActivity
import com.otto.mart.ui.activity.transaction.withdraw.WithdrawActivity
import kotlinx.android.synthetic.main.activity_list_multibank.*
import kotlinx.android.synthetic.main.toolbar.*

open class SelectAccountActivityv2 : SelectAccountMultiBankActivity() {

    val presener: ListLinkedBankAccountPresenter = ListLinkedBankAccountPresenterImpl(this)
    var selectedPos = -1;

    override fun callListAccounBank() {
        if (!presener.checkData())
            presener.loadBanklistAPI()
        else
            presener.loadIssuerBalance()
        displayListData2()
    }

    override fun displayListData(dataAccount: MutableList<AccountListResponse>) {
        //do nothing
    }

    @SuppressLint("NotifyDataSetChanged")
    open fun displayListData2() {
        presener.getDataAccount().observe(this, Observer {
            (reva.adapter as SelectAccountBankAdapter2).mDataSet = it
            reva.adapter?.notifyDataSetChanged()
        })
        btnToolbarBack.setOnClickListener {

            onBackPressed()
            setResult(RESULT_CANCELED)
            finish()
        }

        reva.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            this,
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )

        val adapter = SelectAccountBankAdapter2(presener.getDataAccount().value)
        adapter.listener = ::onBankSelected
        adapter.setOnRetryRequestListener(object :
            SelectAccountBankAdapter2.OnRetryViewClickListener {
            override fun onRetryViewClickListener(pos: Int) {
                presener.onRequestRetry(pos)
            }
        })
        reva.adapter = adapter
        btnSelected.setOnClickListener(null)
        btnSelected.setOnClickListener {
                val intentResult = Intent().apply {
                    putExtra(WithdrawActivity.KEY_BA_POS, selectedPos)
                }
                setResult(RESULT_OK, intentResult)
                finish()
        }
    }

    private fun onBankSelected(pos: Int) {
        selectedPos = pos
        btnSelected.isEnabled = true
        btnSelected.background = ContextCompat.getDrawable(this, R.drawable.button_primary_selector)
    }

}
