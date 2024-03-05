package com.otto.mart.ui.Partials.adapter.multibank

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.otto.mart.R
import com.otto.mart.model.APIModel.Response.multibank.AccountListResponse
import com.otto.mart.support.util.DataUtil
import kotlinx.android.synthetic.main.item_list_linked_account.view.*

class LinkedAccountAdapter(
    var mDataset: MutableList<AccountListResponse.Data>
) : androidx.recyclerview.widget.RecyclerView.Adapter<LinkedAccountAdapter.ViewHolder>() {

    var mOnViewClickListener: OnViewClickListener? = null

    class ViewHolder(val v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LinkedAccountAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_linked_account, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = mDataset[position].accountName
        val accounNumber = mDataset[position].accountNumber
        val accountType = mDataset[position].accountType
        val logo = mDataset[position].logo


        holder.v.tv_name_bank.text = name
        holder.v.layout_shimmer_loading.startShimmerAnimation()
        holder.v.tv_no_rekening.text = DataUtil.setFormatAccountNumber(accounNumber)
        holder.v.tv_status.text = accountType
        holder.v.iv_right.setOnClickListener {
            mOnViewClickListener?.onViewClickListener(position)
        }

        Glide.with(holder.v.iv_logo_bank.context)
            .load(logo)
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
        fun onViewClickListener(position: Int)
    }


}