package com.otto.mart.ui.Partials.adapter.ppob

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.otto.mart.R
import com.otto.mart.model.APIModel.Response.donasi.ProductDonasiResponse

/**
 * Created by macari on 3/7/18.
 */

class PpobDonasiProductAdapter// Provide a suitable constructor (depends on the kind of dataset)
(private val mContext: Context, var mDataset: List<ProductDonasiResponse.Data.Denomination>) : androidx.recyclerview.widget.RecyclerView.Adapter<PpobDonasiProductAdapter.ViewHolder>() {

    var mOnViewClickListener: OnViewClickListener? = null

    class ViewHolder(v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v) {
        // each data item is just a string in this case
        var itemLayout: LinearLayout
        var tvTitle: TextView
        var imgLogo: ImageView

        init {
            itemLayout = v.findViewById(R.id.itemLayout)
            tvTitle = v.findViewById(R.id.tv_title)
            imgLogo = v.findViewById(R.id.img_logo)
        }
    }


    // Create new views (invoked by the layout manager)x
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): PpobDonasiProductAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_ppob_donasi_product, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataset[position]

        Glide.with(mContext)
                .load(item.images)
                .apply(RequestOptions().placeholder(R.drawable.border_white)
                        .error(R.drawable.border_white))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                .into(holder.imgLogo)

        holder.tvTitle.text = item.operator

        holder.itemLayout.setOnClickListener {
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
        fun onViewClickListener(item: ProductDonasiResponse.Data.Denomination, position: Int)
    }
}