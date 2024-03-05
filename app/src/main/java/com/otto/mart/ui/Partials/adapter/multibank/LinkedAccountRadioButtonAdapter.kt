package com.otto.mart.ui.Partials.adapter.multibank

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.otto.mart.R
import com.otto.mart.model.APIModel.Response.multibank.AccountListResponse
import com.otto.mart.support.util.DataUtil
import kotlinx.android.synthetic.main.item_bank_selection.view.*

class LinkedAccountRadioButtonAdapter(
    var mDataset: MutableList<AccountListResponse.Data>
) : androidx.recyclerview.widget.RecyclerView.Adapter<LinkedAccountRadioButtonAdapter.VH>() {

    var isFromDeposit = false
    lateinit var onDeleteBank: (bank: AccountListResponse.Data) -> Unit

    var banks: MutableList<AccountListResponse.Data>? = mutableListOf()
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

    var listener: ((AccountListResponse.Data) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): VH =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_bank_selection, parent, false))

    override fun getItemCount(): Int = banks?.size ?: 0

    override fun onBindViewHolder(vh: VH, position: Int) {
        banks?.let { vh.bind(it[position]) }
    }

    inner class VH(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        fun bind(bank: AccountListResponse.Data) {


            val item = mDataset[position]
            val accounNumber = item.accountNumber



            itemView.setOnClickListener { itemView.rbSelection.performClick() }
            itemView.rbSelection.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    viewSelected = itemView.rbSelection
                    listener?.invoke(bank)
                }
            }

            itemView.tvBankName.text = item.accountName
            itemView.tvAccountNumber.text = DataUtil.setFormatAccountNumber(accounNumber)
          //  itemView.tvBeneficiaryName.text = item.bankName





            itemView.ivDelete.setOnClickListener {
                if (::onDeleteBank.isInitialized) {
                    onDeleteBank(bank)
                }
            }

//            if (bank.id = selectedItemId) {
//                viewSelected = itemView.rbSelection
//            } else {
//                itemView.rbSelection.isChecked = false
//            }
        }
    }
}

