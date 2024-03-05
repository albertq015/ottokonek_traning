package com.otto.mart.ui.Partials.adapter.ppob

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.otto.mart.R
import com.otto.mart.model.APIModel.Misc.FavoriteItemModel

/**
 * Created by macari on 3/7/18.
 */

class PpobFavoriteAdapter// Provide a suitable constructor (depends on the kind of dataset)
(private val mContext: Context, var mDataset: List<FavoriteItemModel>) : androidx.recyclerview.widget.RecyclerView.Adapter<PpobFavoriteAdapter.ViewHolder>() {

    var mOnViewClickListener: OnViewClickListener? = null

    class ViewHolder(v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v) {
        // each data item is just a string in this case
        var itemLayout: LinearLayout
        var tvCustNumber: TextView
        var tvEmail: TextView
        var imgDelete: ImageView

        init {
            itemLayout = v.findViewById(R.id.itemLayout)
            tvCustNumber = v.findViewById(R.id.tvCustNumber)
            tvEmail = v.findViewById(R.id.tvEmail)
            imgDelete = v.findViewById(R.id.imgDelete)
        }
    }

    // Create new views (invoked by the layout manager)x
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): PpobFavoriteAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_ppob_favorite, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataset[position]

        if ((item.email == null) || (item.email.equals(""))) {
            holder.tvEmail.text = "-"
        } else {
            holder.tvEmail.text = item.email
        }

        holder.tvCustNumber.text = item.customer_reference

        holder.itemLayout.setOnClickListener {
            mOnViewClickListener?.onViewClickListener(item, position)
        }

        holder.imgDelete.setOnClickListener {
            mOnViewClickListener?.onDeleteListener(item)
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
        fun onViewClickListener(favorite: FavoriteItemModel, position: Int)
        fun onDeleteListener(favorite: FavoriteItemModel)
    }
}