package com.otto.mart.ui.Partials.adapter.multibank

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.bumptech.glide.Glide
import com.otto.mart.R
import com.otto.mart.model.APIModel.Response.multibank.AccountListResponse
import com.otto.mart.support.util.DataUtil
import com.otto.mart.ui.activity.multibank.LLBAViewModel
import kotlinx.android.synthetic.main.item_bank_selection.view.*
import kotlinx.android.synthetic.main.item_bank_selection.view.layout_shimmer_loading
import kotlinx.android.synthetic.main.item_bank_selection.view.llTotalUangBg
import kotlinx.android.synthetic.main.item_bank_selection.view.tvError
import kotlinx.android.synthetic.main.item_bank_selection.view.tv_status
import kotlinx.android.synthetic.main.item_bank_selection.view.tv_total_uang
import kotlinx.android.synthetic.main.item_list_account_bank.view.*

class BankSelectionAccountListAdapter(
    var mDataset: MutableList<LLBAViewModel>
) : androidx.recyclerview.widget.RecyclerView.Adapter<BankSelectionAccountListAdapter.VH>() {

    lateinit var onDeleteBank: (bank: AccountListResponse.Data) -> Unit
    var mRetryListener: OnRetryViewCLickListener? = null
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

            val name = mDataset[position].object1?.accountName
            val accounNumber = mDataset[position].object1?.accountNumber
            val status = mDataset[position].object1?.accountType

            val logo = mDataset[position].object1?.logo
            val responCode = mDataset[position].object2?.errorCodeResponse
            itemView.layout_shimmer_loading.visibility = View.VISIBLE
            itemView.layout_shimmer_loading.startShimmerAnimation()
            itemView.tv_status.text = status

            if (mDataset[position].object2 != null) {
                var part1 = mDataset[position].object2?.formatedBalance
                val part2 = mDataset[position].object2?.currencyCode
                val error = mDataset[position].object2?.errorval


                if (!error.isNullOrEmpty()) {
                    itemView.tvError.visibility = View.VISIBLE
                    itemView.tvError.text = "Failed to load data, tap to retry"
                    itemView.layout_shimmer_loading.stopShimmerAnimation()
                    itemView.llTotalUangBg.visibility = View.GONE

                    itemView.tvError.setOnClickListener {
                        mRetryListener?.onRetryViewClickListener(position)
                        itemView.tvError.visibility = View.GONE
                        itemView.layout_shimmer_loading.startShimmerAnimation()
                        itemView.llTotalUangBg.visibility = View.VISIBLE
                    }

                } else {
                    itemView.tv_total_uang.visibility = View.VISIBLE
                    itemView.tv_total_uang.text = part1
                    itemView.layout_shimmer_loading.stopShimmerAnimation()
                    itemView.llTotalUangBg.visibility = View.GONE
                }
            }


            itemView.setOnClickListener { itemView.rbSelection.performClick() }
            itemView.rbSelection.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    viewSelected = itemView.rbSelection
                    listener?.invoke(bank)
                }
            }

            itemView.tvBankName.text = name
            itemView.tvAccountNumber.text = DataUtil.setFormatAccountNumber(accounNumber)




            Glide.with(itemView.ivBank)
                .load(logo)
                .into(itemView.ivBank)

            itemView.tvBeneficiaryName.visibility = View.GONE

            itemView.ivDelete.setOnClickListener {
                if (::onDeleteBank.isInitialized) {
                    onDeleteBank(bank)
                }
            }
        }
    }

    fun setOnRetryRequestListener(listener: OnRetryViewCLickListener) {
        this.mRetryListener = listener
    }

    interface OnRetryViewCLickListener {
        fun onRetryViewClickListener(pos: Int)
    }
}
