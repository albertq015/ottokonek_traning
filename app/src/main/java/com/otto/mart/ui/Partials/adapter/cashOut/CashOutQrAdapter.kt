package com.otto.mart.ui.Partials.adapter.cashOut

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.otto.mart.R
import com.otto.mart.model.APIModel.Response.cashOut.CashOutQr
import com.otto.mart.support.util.DataUtil
import kotlinx.android.synthetic.main.item_cash_out_qr.view.*

/**
 * Created by macari on 3/7/18.
 */

class CashOutQrAdapter(private val mContext: Context, var mDataset: List<CashOutQr>) : RecyclerView.Adapter<CashOutQrAdapter.ViewHolder>() {

    var mOnViewClickListener: OnViewClickListener? = null

    class ViewHolder(val v: View) : RecyclerView.ViewHolder(v)

    // Create new views (invoked by the layout manager)x
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_cash_out_qr, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataset[position]

        holder.v.tvStatus.text = item.status
        holder.v.tvDate.text = item.date_time
        holder.v.tvExpiredTime.text = item.expired_time
        holder.v.tvAmount.text = DataUtil.InputDecimal(item.amount.toString())
        holder.v.tvRequestId.text = item.requestId

        holder.v.itemLayout.setOnClickListener {
            mOnViewClickListener?.onViewClickListener(item, position)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return mDataset.size
    }

    fun setmOnViewClickListener(mOnViewClickListener: OnViewClickListener) {
        this.mOnViewClickListener = mOnViewClickListener
    }

    interface OnViewClickListener {
        fun onViewClickListener(data: CashOutQr, position: Int)
    }
}