package com.otto.mart.ui.Partials.adapter.withdraw

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding3.widget.checkedChanges
import com.otto.mart.R
import com.otto.mart.model.localmodel.omzet.OmzetFilter
import com.otto.mart.model.localmodel.omzet.WithdrawMethod
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.notNull
import com.otto.mart.support.util.visible
import kotlinx.android.synthetic.main.activity_topup.view.*
import kotlinx.android.synthetic.main.dialog_header.view.*
import kotlinx.android.synthetic.main.item_omzet_filter.view.*
import kotlinx.android.synthetic.main.item_omzet_filter.view.itemLayout
import kotlinx.android.synthetic.main.item_omzet_filter.view.tvName
import kotlinx.android.synthetic.main.item_withdraw_method.view.*
import kotlinx.android.synthetic.main.product_toolbar.view.*

/**
 * Created by macari on 3/7/18.
 */

class WithdrawMethodAdapter// Provide a suitable constructor (depends on the kind of dataset)
(private val mContext: Context, var mDataset: List<WithdrawMethod>) : androidx.recyclerview.widget.RecyclerView.Adapter<WithdrawMethodAdapter.ViewHolder>() {

    lateinit var itemSelected: (WithdrawMethod, Int) -> Unit

    class ViewHolder(val v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): WithdrawMethodAdapter.ViewHolder {
        var layout = R.layout.item_withdraw_method

        val view = LayoutInflater.from(parent.context)
                .inflate(layout, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataset[position]

        if (item.isEnable) {
            holder.v.itemLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white))
            holder.v.bottomLine.visible()
        } else {
            holder.v.itemLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.super_soft_grey))
            holder.v.bottomLine.gone()
        }

        holder.v.radioButton.isChecked = item.isSelected

        holder.v.tvName.text = item.name
        holder.v.tvDesc.text = item.desc

        holder.v.itemLayout.setOnClickListener {
            if (::itemSelected.isInitialized) {
                if (item.isEnable) {
                    itemSelected(item, position)
                }
            }
        }

        holder.v.radioButton.setOnClickListener {
            if (item.isEnable) {
                holder.v.itemLayout.performClick()
            } else {
                holder.v.radioButton.isChecked = false
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return mDataset.size
    }
}