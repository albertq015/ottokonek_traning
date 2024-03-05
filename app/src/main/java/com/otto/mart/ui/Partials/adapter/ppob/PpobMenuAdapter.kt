package com.otto.mart.ui.Partials.adapter.ppob

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.otto.mart.R
import com.otto.mart.model.localmodel.omzet.OmzetFilter
import com.otto.mart.model.localmodel.ppob.PpobMenu
import kotlinx.android.synthetic.main.item_omzet_filter.view.*
import kotlinx.android.synthetic.main.item_ppob_menu.view.*
import kotlinx.android.synthetic.main.item_ppob_menu.view.itemLayout

/**
 * Created by macari on 3/7/18.
 */

class PpobMenuAdapter// Provide a suitable constructor (depends on the kind of dataset)
(private val mContext: Context, var mDataset: List<PpobMenu>) : androidx.recyclerview.widget.RecyclerView.Adapter<PpobMenuAdapter.ViewHolder>() {

    var mOnViewClickListener: OnViewClickListener? = null
    
    lateinit var itemSelected: (PpobMenu, Int) -> Unit
    
    class ViewHolder(val v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): PpobMenuAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_ppob_menu, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataset[position]
        var icon = R.drawable.icon_button_more
        item.icon?.let { icon = it }

        Glide.with(mContext)
                .load(item.iconUrl)
                .error(icon)
                .into(holder.v.imgIcon)

        holder.v.tvTitle.text = item.title
        
        holder.v.itemLayout.setOnClickListener {
            if (::itemSelected.isInitialized) {
                itemSelected(item, position)
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
        fun onViewClickListener(item: PpobMenu, position: Int)
    }
}