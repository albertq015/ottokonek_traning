package com.otto.mart.ui.Partials.adapter.multibank

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.bumptech.glide.Glide
import com.otto.mart.R
import com.otto.mart.support.util.DataUtil
import com.otto.mart.ui.activity.multibank.LLBAViewModel
import kotlinx.android.synthetic.main.item_list_account_bank.view.*
import kotlinx.android.synthetic.main.item_list_account_bank.view.iv_logo_bank
import kotlinx.android.synthetic.main.item_list_account_bank.view.llTotalUangBg
import kotlinx.android.synthetic.main.item_list_account_bank.view.tvError
import kotlinx.android.synthetic.main.item_list_account_bank.view.tv_name_bank
import kotlinx.android.synthetic.main.item_list_account_bank.view.tv_no_rekening
import kotlinx.android.synthetic.main.item_list_account_bank.view.tv_status
import kotlinx.android.synthetic.main.item_list_account_bank.view.tv_total_uang
import kotlinx.android.synthetic.main.item_list_linked_account.view.*

class SelectAccountCashOutAdapter(var mDataSet: MutableList<LLBAViewModel>?) :
    androidx.recyclerview.widget.RecyclerView.Adapter<SelectAccountCashOutAdapter.ViewHolder>() {

    var listener: ((Int) -> Unit)? = null
    var retryListener: OnRetryViewClickListener? = null
    private var viewSelected: RadioButton? = null
        set(value) {
            field?.isChecked = false
            field = value
            field?.isChecked = true
        }

    fun setOnRetryRequestListener(a: OnRetryViewClickListener) {
        retryListener = a
    }

    class ViewHolder(val v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_account_bank, parent, false)
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bind(holder.v, mDataSet?.get(position), position)
    }

    private fun bind(itemView: View, bank: LLBAViewModel?, pos: Int) {


        itemView.radioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewSelected = itemView.radioButton
                listener?.invoke(pos)
            }
        }

        val nama = bank?.object1?.binName
        val status = bank?.object1?.accountType

        itemView.tv_name_bank.text = nama
        itemView.tv_no_rekening.text = DataUtil.setFormatAccountNumber(bank?.object1?.accountNumber)
        itemView.tv_status.text = status
        settleDisplayForBalance(itemView, bank, pos)


        Glide.with(itemView.iv_logo_bank.context)
            .load(bank?.object1?.logo)
            .error(com.otto.mart.R.drawable.icon_blank)
            .placeholder(com.otto.mart.R.drawable.icon_blank)
            .into(itemView.iv_logo_bank)




    }

    open fun settleDisplayForBalance(itemView: View, bank: LLBAViewModel?, pos: Int) {
        val it1 = bank?.object2?.formatedBalance
        val error = bank?.object2?.errorval
        itemView.shimmerItem.startShimmerAnimation()
        if (!bank?.object2?.currencyCode.isNullOrEmpty()) {
            itemView.tvError.visibility = View.GONE
            itemView.tv_total_uang.visibility = View.VISIBLE
            itemView.shimmerItem.stopShimmerAnimation()
            itemView.tv_total_uang.setTextColor(Color.parseColor("#006841"))
            itemView.tv_total_uang.text = "$it1"
            itemView.setOnClickListener {
                itemView.radioButton.performClick()
            }
            itemView.llTotalUangBg.visibility = View.GONE

        } else {
            itemView.tvError.text = "$error"
            itemView.tvError.visibility = View.GONE
            itemView.setOnClickListener {
                itemView.radioButton.performClick()
                retryListener?.onRetryViewClickListener(pos)
            }
            itemView.llTotalUangBg.visibility = View.VISIBLE

            Glide.with(itemView.iv_logo_bank.context)
                .load(bank?.object1?.logo)
                .error(com.otto.mart.R.drawable.icon_blank)
                .placeholder(com.otto.mart.R.drawable.icon_blank)
                .into(itemView.iv_logo_bank)


        }
    }

    override fun getItemCount(): Int = mDataSet?.size ?: 0

    interface OnRetryViewClickListener {
        fun onRetryViewClickListener(pos: Int)
    }

}

