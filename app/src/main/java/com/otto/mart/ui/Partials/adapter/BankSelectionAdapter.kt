package com.otto.mart.ui.Partials.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.bumptech.glide.Glide
import com.otto.mart.R
import com.otto.mart.model.APIModel.Misc.bank.BankAccountModel
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.visible
import kotlinx.android.synthetic.main.item_bank_selection.view.*

class BankSelectionAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<BankSelectionAdapter.VH>() {

    var isFromDeposit = false
    lateinit var onDeleteBank: (bank: BankAccountModel) -> Unit

    var banks: MutableList<BankAccountModel>? = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var selectedItemId = 0
    private var viewSelected: RadioButton? = null
        set(value) {
            field?.isChecked = false
            field = value
            field?.isChecked = true
        }

    var listener: ((BankAccountModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): VH =
            VH(LayoutInflater.from(parent.context).inflate(R.layout.item_bank_selection, parent, false))

    override fun getItemCount(): Int = banks?.size ?: 0

    override fun onBindViewHolder(vh: VH, position: Int) {
        banks?.let { vh.bind(it[position]) }
    }

    inner class VH(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        fun bind(bank: BankAccountModel) {
            itemView.setOnClickListener { itemView.rbSelection.performClick() }
            itemView.rbSelection.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    viewSelected = itemView.rbSelection
                    listener?.invoke(bank)
                }
            }

            itemView.tvBankName.text = bank.bankName
            itemView.tvAccountNumber.text = bank.accountNumber
            if (isFromDeposit) {
                itemView.ivDelete.visible()
                itemView.tvBeneficiaryName.text = bank.accountName
            } else {
                itemView.ivDelete.gone()
                itemView.tvBeneficiaryName.text = bank.accountOwner
            }

            if (bank.resLogo != 0) {
                Glide.with(itemView.ivBank.context)
                        .load(bank.resLogo)
                        .into(itemView.ivBank)
            } else {
                Glide.with(itemView.ivBank.context)
                        .load(bank.bankLogo)
                        .into(itemView.ivBank)
            }

            itemView.ivDelete.setOnClickListener {
                if (::onDeleteBank.isInitialized) {
                    onDeleteBank(bank)
                }
            }

            if (bank.id == selectedItemId) {
                viewSelected = itemView.rbSelection
            } else {
                itemView.rbSelection.isChecked = false
            }
        }
    }
}
