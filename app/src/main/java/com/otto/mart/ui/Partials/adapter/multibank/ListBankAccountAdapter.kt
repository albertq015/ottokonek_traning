package com.otto.mart.ui.Partials.adapter.multibank


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.otto.mart.R
import com.otto.mart.model.APIModel.Response.multibank.BankTransferListResponse
import com.otto.mart.support.util.DataUtil
import kotlinx.android.synthetic.main.item_list_bank_account_profile.view.*
import kotlinx.android.synthetic.main.item_list_bank_account_profile.view.iv_logo_bank
import kotlinx.android.synthetic.main.item_list_linked_account.view.*
import kotlinx.android.synthetic.main.item_list_linked_account.view.tv_no_rekening

class ListBankAccountAdapter(

    var mDataset: List<BankTransferListResponse.Data>
) : androidx.recyclerview.widget.RecyclerView.Adapter<ListBankAccountAdapter.ViewHolder>() {

    var mOnViewClickListener: OnViewClickListener? = null

    class ViewHolder(val v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListBankAccountAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_bank_account_profile, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataset[position]
        val accounNumber = item.accountNumber




        holder.v.tv_name_user.text = item.accountName
        holder.v.tv_no_rekening.text = DataUtil.setFormatAccountNumber(accounNumber)
        holder.v.tv_bank_name.text = item.bankName
        Glide.with(holder.v.iv_logo_bank.context)
            .load(item.logo)
            .into(holder.v.iv_logo_bank)


    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return mDataset.size
    }

    fun setmOnViewClickListener(mOnViewClickListener: OnViewClickListener) {
        this.mOnViewClickListener = mOnViewClickListener
    }

    interface OnViewClickListener {
        fun onViewClickListener(data: BankTransferListResponse.Data, position: Int)
    }


}