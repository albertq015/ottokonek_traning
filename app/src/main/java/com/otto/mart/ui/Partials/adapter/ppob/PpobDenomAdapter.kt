package com.otto.mart.ui.Partials.adapter.ppob

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.otto.mart.R
import com.otto.mart.model.APIModel.Misc.OttoagDenomModel
import com.otto.mart.support.util.DataUtil
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.visible
import kotlinx.android.synthetic.main.item_ppob_denom.view.*

/**
 * Created by macari on 3/7/18.
 */

class PpobDenomAdapter// Provide a suitable constructor (depends on the kind of dataset)
(private val mContext: Context, var mDataset: List<OttoagDenomModel>) : androidx.recyclerview.widget.RecyclerView.Adapter<PpobDenomAdapter.ViewHolder>() {

    var mOnViewClickListener: OnViewClickListener? = null

    class ViewHolder(val v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): PpobDenomAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_ppob_denom, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataset[position]

        if (item.isSelected) {
            holder.v.itemLayout.background = ContextCompat.getDrawable(mContext, R.drawable.button_primary_selector)
            holder.v.tvName.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            holder.v.tvPrice.setTextColor(ContextCompat.getColor(mContext, R.color.white))
        } else {
            holder.v.itemLayout.background = ContextCompat.getDrawable(mContext, R.drawable.button_white_grey_selector)
            holder.v.tvName.setTextColor(ContextCompat.getColor(mContext, R.color.text_blue))
            holder.v.tvPrice.setTextColor(ContextCompat.getColor(mContext, R.color.text_blue))
        }

        if(item.isDisabled){
            holder.v.itemLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.very_light_grey))
            holder.v.tvName.setTextColor(ContextCompat.getColor(mContext, R.color.text_brown))
            holder.v.tvPrice.setTextColor(ContextCompat.getColor(mContext, R.color.text_brown))
        }

        if (item.cashback_omzet != null) {
            if (item.cashback_omzet != 0L){
                holder.v.tvCashback.text = "Cashback: " + DataUtil.convertCurrency(item.cashback_omzet)
                holder.v.tvCashback.visible()
            } else {
                holder.v.tvCashback.gone()
            }
        }

        holder.v.tvName.text = item.product_name
        holder.v.tvPrice.text = DataUtil.convertCurrency(item.price.toString())

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
        fun onViewClickListener(fsvorite: OttoagDenomModel, position: Int)
    }
}