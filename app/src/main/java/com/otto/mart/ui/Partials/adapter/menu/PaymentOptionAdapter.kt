package com.otto.mart.ui.Partials.adapter.menu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.otto.mart.R
import com.otto.mart.model.localmodel.ppob.PpobMenu
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.visible
import kotlinx.android.synthetic.main.item_payment_option.view.*
import kotlinx.android.synthetic.main.item_ppob_menu.view.imgIcon
import kotlinx.android.synthetic.main.item_ppob_menu.view.itemLayout
import kotlinx.android.synthetic.main.item_ppob_menu.view.tvTitle

/**
 * Created by macari on 3/7/18.
 */

class PaymentOptionAdapter// Provide a suitable constructor (depends on the kind of dataset)
(private val mContext: Context, var mDataset: List<PpobMenu>) : androidx.recyclerview.widget.RecyclerView.Adapter<PaymentOptionAdapter.ViewHolder>() {

    var mOnViewClickListener: OnViewClickListener? = null

    lateinit var itemSelected: (PpobMenu, Int) -> Unit

    class ViewHolder(val v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): PaymentOptionAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_payment_option, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataset[position]
        var icon = R.drawable.icon_button_more
        item.icon?.let { icon = it }

        holder.v.line.visible()
        if ((position + 1) == itemCount) {
            holder.v.line.gone()
        }

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