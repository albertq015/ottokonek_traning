package com.otto.mart.ui.Partials.adapter.multibank

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.otto.mart.R
import com.otto.mart.support.util.DataUtil
import com.otto.mart.ui.activity.multibank.LLBAViewModel
import kotlinx.android.synthetic.main.item_list_linked_account.view.*

class LinkedAccountAdapter2(
    var mDataset: MutableList<LLBAViewModel>
) : androidx.recyclerview.widget.RecyclerView.Adapter<LinkedAccountAdapter2.ViewHolder>() {

    var mOnViewClickListener: OnViewClickListener? = null
    var mRetryListener: OnRetryViewCLickListener? = null

    class ViewHolder(val v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_linked_account, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = mDataset[position].object1?.binName
        val accounNumber = mDataset[position].object1?.accountNumber
        val accountType = mDataset[position].object1?.accountType
        val logo = mDataset[position].object1?.logo
        val responCode = mDataset[position].object2?.errorCodeResponse

        holder.v.tv_name_bank.text = name
        holder.v.layout_shimmer_loading.startShimmerAnimation()
        if (mDataset[position].object2 != null) {

            var part1 = mDataset[position].object2?.formatedBalance
            val part2 = mDataset[position].object2?.currencyCode
            val error = mDataset[position].object2?.errorval
            holder.v.iv_right.visibility = View.GONE

            if (!error.isNullOrEmpty()) {
                holder.v.tvError.visibility = View.VISIBLE
                holder.v.tvError.text = "Failed to load data, tap to retry"
                holder.v.layout_shimmer_loading.stopShimmerAnimation()
                holder.v.llTotalUangBg.visibility = View.GONE

                holder.v.tvError.setOnClickListener {
                    mRetryListener?.onRetryViewClickListener(position)
                    holder.v.tvError.visibility = View.GONE
                    holder.v.layout_shimmer_loading.startShimmerAnimation()
                    holder.v.llTotalUangBg.visibility = View.VISIBLE
                }

                if (responCode == 498) {
                    holder.v.iv_right.visibility = View.GONE
                    holder.v.tvError.text = error
                    holder.v.itemLayout.setOnClickListener {

                    }
                }

            } else {
                holder.v.tv_total_uang.visibility = View.VISIBLE
                holder.v.tv_total_uang.text = part1
                holder.v.layout_shimmer_loading.stopShimmerAnimation()
                holder.v.llTotalUangBg.visibility = View.GONE

            }
        }
        holder.v.tv_no_rekening.text = DataUtil.setFormatAccountNumber(accounNumber)
        holder.v.tv_status.text = accountType

        holder.v.itemLayout.setOnClickListener {
            mOnViewClickListener?.onViewClickListener(position, accounNumber!!, responCode)

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

    fun setOnRetryRequestListener(listener: OnRetryViewCLickListener) {
        this.mRetryListener = listener
    }

    interface OnViewClickListener {
        fun onViewClickListener(position: Int, accountNumber: String, responCode: Int?)
    }

    interface OnRetryViewCLickListener {
        fun onRetryViewClickListener(pos: Int)
    }
}