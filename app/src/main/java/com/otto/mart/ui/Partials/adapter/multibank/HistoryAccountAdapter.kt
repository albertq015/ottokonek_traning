package com.otto.mart.ui.Partials.adapter.multibank

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.otto.mart.OttoMartApp
import com.otto.mart.R
import com.otto.mart.model.APIModel.Response.multibank.HistoryTransactionAccount
import com.otto.mart.support.util.DataUtil
import kotlinx.android.synthetic.main.activity_payment_success.*

import kotlinx.android.synthetic.main.item_trasaction_history.view.*
import java.util.*

class HistoryAccountAdapter
    (private val mContext: Context, var mDataset: List<HistoryTransactionAccount>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<HistoryAccountAdapter.ViewHolder>() {

    var mOnViewClickListener: OnViewClickListener? = null
    var isFromOmzet = false

    class ViewHolder(val v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryAccountAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_trasaction_history, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataset[position]
        val amount = DataUtil.InputDecimal(item.amount.toString().replace(",", "."))
            .replace(mContext.getString(R.string.text_currency), "").trim()



        holder.v.tvDate.setText(item.entry_date)
//        holder.v.tvDate.setText(
//            DataUtil.getFormattedDateStringFromServerResponse2(
//                item.entry_date,
//                "dd-MM-YYYY HH:mm aa"
//            )
//        )
        holder.v.tvNoResi.setText(item.reference_number)

        if (item.transaction_indicator.equals("C", ignoreCase = true)) {
            holder.v.ivFlow.setImageDrawable(
                ContextCompat.getDrawable(
                    mContext,
                    R.drawable.ic_history_debit
                )
            )
            holder.v.tvAmount.setText("+ " + amount)
            holder.v.tvStatus.setText("Transfer")
            holder.v.tvAmount.setTextColor(ContextCompat.getColor(OttoMartApp.getContext(), R.color.green_dark_4))
            holder.v.tvStatus.setTextColor(
                ContextCompat.getColor(
                    OttoMartApp.getContext(),
                    R.color.green_dark_4
                )
            )
        } else {
            holder.v.ivFlow.setImageDrawable(
                ContextCompat.getDrawable(
                    mContext,
                    R.drawable.ic_history_credit
                )
            )
            holder.v.tvAmount.setTextColor(
                ContextCompat.getColor(
                    OttoMartApp.getContext(),
                    R.color.red_dark_4
                )
            )
            holder.v.tvStatus.setText("Account")
            holder.v.tvAmount.setText("- " + amount)
            holder.v.tvStatus.setTextColor(
                ContextCompat.getColor(
                    OttoMartApp.getContext(),
                    R.color.red_dark_4
                )
            )
        }


        holder.v.tvDesc.setTextColor(
            ContextCompat.getColor(
                OttoMartApp.getContext(),
                R.color.text_blue
            )
        )
        holder.v.tvDesc.setText(item.description)


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
        fun onViewClickListener(item: HistoryTransactionAccount, position: Int)
    }

}