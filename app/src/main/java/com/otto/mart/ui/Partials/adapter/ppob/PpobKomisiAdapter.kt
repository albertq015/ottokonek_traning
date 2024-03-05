package com.otto.mart.ui.Partials.adapter.ppob

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.otto.mart.R
import com.otto.mart.support.util.DataUtil

/**
 * Created by macari on 3/7/18.
 */

class PpobKomisiAdapter// Provide a suitable constructor (depends on the kind of dataset)
(private val mContext: Context, var mDataset: List<Int>) : androidx.recyclerview.widget.RecyclerView.Adapter<PpobKomisiAdapter.ViewHolder>() {

    var mOnViewClickListener: OnViewClickListener? = null

    class ViewHolder(v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v) {
        var itemLayout: LinearLayout
        var tvKomisi: TextView

        init {
            itemLayout = v.findViewById(R.id.itemLayout)
            tvKomisi = v.findViewById(R.id.tvKomisi)
        }
    }

    // Create new views (invoked by the layout manager)x
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): PpobKomisiAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_ppob_komisi, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataset[position]

        holder.tvKomisi.text = DataUtil.convertCurrency(item)

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
        fun onViewClickListener(item: Int, position: Int)
    }
}