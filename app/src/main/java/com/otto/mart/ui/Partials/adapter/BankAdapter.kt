package com.otto.mart.ui.Partials.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.bumptech.glide.Glide
import com.otto.mart.R
import com.otto.mart.model.APIModel.Response.bank.BankOKKItem
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.isVisible
import com.otto.mart.ui.fragment.profile.BankListFragment
import kotlinx.android.synthetic.main.item_bank_selection.view.*

class BankAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<BankAdapter.VH>() {

    var isPending = false
    var currentStatus = BankListFragment.rejectedType
    var onDeleteBank: ((bank: BankOKKItem) -> Unit)? = null

    var banks: List<BankOKKItem>? = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var selectedItemId = ""
    private var viewSelected: RadioButton? = null
        set(value) {
            field?.isChecked = false
            field = value
            field?.isChecked = true
        }

    var selectedListener: ((BankOKKItem) -> Unit)? = null
    var actionListener: ((BankOKKItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): VH =
            VH(LayoutInflater.from(parent.context).inflate(R.layout.item_bank_selection, parent, false))

    override fun getItemCount(): Int = banks?.size ?: 0

    override fun onBindViewHolder(vh: VH, position: Int) {
        banks?.let { vh.bind(it[position]) }
    }

    inner class VH(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        fun bind(bank: BankOKKItem) {
            itemView.setOnClickListener { itemView.rbSelection.performClick() }
            itemView.rbSelection.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    viewSelected = itemView.rbSelection
                    selectedListener?.invoke(bank)
                }
            }

            itemView.ivDelete.setOnClickListener {
                actionListener?.invoke(bank)
            }

            itemView.tvBankName.text = bank.bankName
            itemView.tvAccountNumber.text = bank.accountNumber
            itemView.tvBeneficiaryName.text = bank.accountName
            itemView.ivDelete.setImageResource(R.drawable.img_edit)

            itemView.ivDelete.isVisible(currentStatus == BankListFragment.approvedType || currentStatus == BankListFragment.rejectedType)
            itemView.rbSelection.isVisible(currentStatus == BankListFragment.approvedType)

            if (bank.resLogo != 0) {
                Glide.with(itemView.ivBank.context)
                        .load(bank.resLogo)
                        .into(itemView.ivBank)
            } else {
                Glide.with(itemView.ivBank.context)
                        .load(bank.urlImage)
                        .into(itemView.ivBank)
            }

            if (isPending) {
                itemView.rbSelection.gone()
            }

            itemView.ivDelete.setOnClickListener {
                actionListener?.invoke(bank)
            }

            if (bank.bankCode == selectedItemId) {
                viewSelected = itemView.rbSelection
            } else {
                itemView.rbSelection.isChecked = false
            }
        }
    }
}
