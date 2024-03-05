package com.otto.mart.ui.Partials.adapter.omzet

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.otto.mart.R
import com.otto.mart.model.localmodel.omzet.OmzetFilter
import com.otto.mart.support.util.notNull
import kotlinx.android.synthetic.main.dialog_header.view.*
import kotlinx.android.synthetic.main.item_omzet_filter.view.*
import kotlinx.android.synthetic.main.product_toolbar.view.*

/**
 * Created by macari on 3/7/18.
 */

class OmzetFilterAdapter// Provide a suitable constructor (depends on the kind of dataset)
(private val mContext: Context, var mDataset: List<OmzetFilter>) : androidx.recyclerview.widget.RecyclerView.Adapter<OmzetFilterAdapter.ViewHolder>() {

    lateinit var itemSelected: (OmzetFilter, Int) -> Unit

    class ViewHolder(val v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): OmzetFilterAdapter.ViewHolder {
        var layout = R.layout.item_omzet_filter

        val view = LayoutInflater.from(parent.context)
                .inflate(layout, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataset[position]

        if (item.isSelected) {
            holder.v.tvName.setTextColor(ContextCompat.getColor(mContext, R.color.dark_sky_blue))
            holder.v.itemLayout.background = ContextCompat.getDrawable(mContext, R.drawable.filter_selected_bg)
        } else {
            holder.v.tvName.setTextColor(ContextCompat.getColor(mContext, R.color.text_blue))
            holder.v.itemLayout.background = ContextCompat.getDrawable(mContext, R.drawable.filter_bg)
        }

        holder.v.tvName.text = item.name

        holder.v.itemLayout.setOnClickListener {
            if (::itemSelected.isInitialized) {
                itemSelected(item, position)
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return mDataset.size
    }
}