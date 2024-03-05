package com.otto.mart.ui.Partials.adapter.ppob

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.otto.mart.R
import com.otto.mart.model.localmodel.ppob.PpobProductType
import kotlinx.android.synthetic.main.item_ppob_category.view.itemLayout
import kotlinx.android.synthetic.main.item_ppob_product_type.view.*

/**
 * Created by macari on 3/7/18.
 */

class PpobProductTypeAdapter// Provide a suitable constructor (depends on the kind of dataset)
(private val mContext: Context, var mDataset: List<PpobProductType>) : androidx.recyclerview.widget.RecyclerView.Adapter<PpobProductTypeAdapter.ViewHolder>() {

    var mOnViewClickListener: OnViewClickListener? = null

    class ViewHolder(val v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): PpobProductTypeAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_ppob_product_type, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataset[position]

        Glide.with(mContext)
                .load(item.icon)
                .apply(RequestOptions().placeholder(R.drawable.border_white)
                        .error(R.drawable.border_white))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                .into(holder.v.imgLogo)

        holder.v.tvName.text = item.name

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
        fun onViewClickListener(provider: PpobProductType, position: Int)
    }
}