package com.otto.mart.ui.Partials.adapter.omzet

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.otto.mart.OttoMartApp
import com.otto.mart.R
import com.otto.mart.model.APIModel.Misc.history.OmzetHistory
import com.otto.mart.support.util.DataUtil
import kotlinx.android.synthetic.main.item_trasaction_history.view.*

/**
 * Created by macari on 3/7/18.
 */

class OmzetHistoryAdapter // Provide a suitable constructor (depends on the kind of dataset)
(private val mContext: Context, var mDataset: List<OmzetHistory>) : androidx.recyclerview.widget.RecyclerView.Adapter<OmzetHistoryAdapter.ViewHolder>() {

    var mOnViewClickListener: OnViewClickListener? = null
    var isFromOmzet = false

    class ViewHolder(val v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): OmzetHistoryAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_trasaction_history, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataset[position]

        val amount = DataUtil.InputDecimal(item.amount.toString().replace(",", "."))
                .replace(mContext.getString(R.string.text_currency), "").trim()

        if (isFromOmzet) {
            holder.v.tvDate.setText(item.date_string)
            holder.v.tvNoResi.setText(item.no_resi)

            if (item.direction.equals("in", ignoreCase = true)) {
                holder.v.ivFlow.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_history_debit))
                holder.v.tvAmount.setText("+ " + amount)
                if (item.payment_method?.toLowerCase() == "cash") {
                    holder.v.ivFlow.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_history_cash))
                    holder.v.tvAmount.setTextColor(ContextCompat.getColor(OttoMartApp.getContext(), R.color.ocean_blue_4))
                } else {
                    holder.v.tvAmount.setTextColor(ContextCompat.getColor(OttoMartApp.getContext(), R.color.green_dark_4))
                }
            } else {
                holder.v.ivFlow.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_history_credit))
                holder.v.tvAmount.setText("- " + amount)
                holder.v.tvAmount.setTextColor(ContextCompat.getColor(OttoMartApp.getContext(), R.color.red_dark_4))
            }
        } else {
            holder.v.tvDate.setText(item.posting_date)
            holder.v.tvNoResi.setText(item.reference_number)

            if (item.transaction_indicator.equals("C", ignoreCase = true)) {
                holder.v.ivFlow.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_history_debit))
                holder.v.tvAmount.setText("+ " + amount)

                if (item.payment_method?.toLowerCase() == "cash") {
                    holder.v.ivFlow.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_history_cash))
                    holder.v.tvAmount.setTextColor(ContextCompat.getColor(OttoMartApp.getContext(), R.color.ocean_blue_4))
                } else {
                    holder.v.tvAmount.setTextColor(ContextCompat.getColor(OttoMartApp.getContext(), R.color.green_dark_4))
                }
            } else {
                holder.v.ivFlow.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_history_credit))
                holder.v.tvAmount.setTextColor(ContextCompat.getColor(OttoMartApp.getContext(), R.color.red_dark_4))

                holder.v.tvAmount.setText("- " + amount)
            }
        }

        holder.v.tvDesc.setTextColor(ContextCompat.getColor(OttoMartApp.getContext(), R.color.text_blue))
        holder.v.tvDesc.setText(item.description)

        holder.v.tvStatus.setText(item.payment_method)
        if (item.direction.equals("Transfer ke Bank", ignoreCase = true)) {
            holder.v.tvStatus.setText(holder.v.context.getString(R.string.text_transfer))
        }

        holder.v.itemLayout.setOnClickListener {
            mOnViewClickListener?.let {
                it.onViewClickListener(item, position)
            }
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
        fun onViewClickListener(item: OmzetHistory, position: Int)
    }
}