package com.otto.mart.ui.Partials.adapter.multibank

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.bumptech.glide.Glide
import com.otto.mart.R
import com.otto.mart.model.APIModel.Response.multibank.AccountListResponse
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.visible
import kotlinx.android.synthetic.main.item_list_account_bank.view.*

open class SelectAccountBankAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<SelectAccountBankAdapter.VH>() {

    open var mDataSet: MutableList<AccountListResponse>? = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var selectedItemId = 0
    var listener: ((AccountListResponse) -> Unit)? = null
    private var viewSelected: RadioButton? = null
        set(value) {
            field?.isChecked = false
            field = value
            field?.isChecked = true
        }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): VH =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_list_account_bank, parent, false))

//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): SelectAccountBankAdapter.ViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_list_account_bank, parent, false)
//        return ViewHolder(view)
//    }

    inner class VH(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        fun bind(bank: AccountListResponse) {
            itemView.setOnClickListener { itemView.radioButton.performClick() }
            itemView.radioButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    viewSelected = itemView.radioButton
                    listener?.invoke(bank)
                }
            }
            val nama = bank.data[position].binName
            val number = bank.data[position].accountNumber
            val status = bank.data[position].status

            itemView.tv_name_bank.text = nama
            itemView.tv_no_rekening.text = number
            itemView.tv_status.text = status

//            if (isFromDeposit) {
//                itemView.ivDelete.visible()
//                itemView.tvBeneficiaryName.text = bank.accountName
//            } else {
//                itemView.ivDelete.gone()
//                itemView.tvBeneficiaryName.text = bank.accountOwner
//            }
//
//            if (bank.resLogo != 0) {
//                Glide.with(itemView.ivBank.context)
//                    .load(bank.resLogo)
//                    .into(itemView.ivBank)
//            } else {
//                Glide.with(itemView.ivBank.context)
//                    .load(bank.bankLogo)
//                    .into(itemView.ivBank)
//            }
//
//            itemView.ivDelete.setOnClickListener {
//                if (::onDeleteBank.isInitialized) {
//                    onDeleteBank(bank)
//                }
//            }
//
//            if (bank.id == selectedItemId) {
//                viewSelected = itemView.rbSelection
//            } else {
//                itemView.rbSelection.isChecked = false
//            }
        }
    }

    override fun getItemCount(): Int = mDataSet?.size ?: 0
    override fun onBindViewHolder(holder: VH, position: Int) {
        mDataSet?.let { holder.bind(it[position]) }
    }


}