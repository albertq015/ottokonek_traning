package com.otto.mart.ui.Partials.adapter.cashOut

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.otto.mart.R
import com.otto.mart.model.APIModel.Response.cashOut.CashOutAmount
import com.otto.mart.model.APIModel.Response.cashOut.CashOutQr
import kotlinx.android.synthetic.main.item_cash_out_amount.view.*
import kotlinx.android.synthetic.main.item_cash_out_qr.view.*
import kotlinx.android.synthetic.main.item_cash_out_qr.view.itemLayout
import kotlinx.android.synthetic.main.item_cash_out_qr.view.tvAmount

/**
 * Created by macari on 3/7/18.
 */

class CashOutAmountAdapter(private val mContext: Context, var mDataset: List<CashOutAmount>) : RecyclerView.Adapter<CashOutAmountAdapter.ViewHolder>() {

    var mOnViewClickListener: OnViewClickListener? = null

    class ViewHolder(val v: View) : RecyclerView.ViewHolder(v)

    // Create new views (invoked by the layout manager)x
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_cash_out_amount, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataset[position]

        holder.v.tvAmount.text = item.amount

        item.isSelected?.let {
            if(it){
                holder.v.contentContainer.background = ContextCompat.getDrawable(mContext, R.drawable.button_white_border_green_bg)
            } else {
                holder.v.contentContainer.background = ContextCompat.getDrawable(mContext, R.drawable.button_white_grey_selector)
            }
        }

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
        fun onViewClickListener(data: CashOutAmount, position: Int)
    }
}