package com.otto.mart.ui.Partials.adapter.multibank

import android.view.View
import com.otto.mart.ui.activity.multibank.LLBAViewModel
import kotlinx.android.synthetic.main.item_list_account_bank.view.*

class SelectAccountBankWithdrawAdapter(mDataSet: MutableList<LLBAViewModel>?) :
    SelectAccountBankAdapter2(mDataSet) {

    override fun settleDisplayForBalance(itemView: View, bank: LLBAViewModel?, pos: Int) {
        itemView.shimmerItem.visibility = View.GONE
    }
}