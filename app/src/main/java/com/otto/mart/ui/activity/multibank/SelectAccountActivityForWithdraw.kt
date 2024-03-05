package com.otto.mart.ui.activity.multibank

import android.annotation.SuppressLint
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.otto.mart.R
import com.otto.mart.ui.Partials.adapter.multibank.SelectAccountBankWithdrawAdapter
import com.otto.mart.ui.activity.transaction.withdraw.WithdrawActivity.Companion.KEY_BA_POS
import kotlinx.android.synthetic.main.activity_list_multibank.*
import kotlinx.android.synthetic.main.toolbar.*

class SelectAccountActivityForWithdraw : SelectAccountActivityv2() {

    @SuppressLint("NotifyDataSetChanged")
    override fun displayListData2() {
        presener.getDataAccount().observe(this, Observer {
            (reva.adapter as SelectAccountBankWithdrawAdapter).mDataSet = it
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

        val adapter = SelectAccountBankWithdrawAdapter(presener.getDataAccount().value)
        adapter.listener = ::onBankSelected

        reva.adapter = adapter
        btnSelected.setOnClickListener(null)
        btnSelected.setOnClickListener {
            val intentResult = Intent().apply {
                putExtra(KEY_BA_POS, selectedPos)
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