package com.otto.mart.ui.Partials.adapter.ppob

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import com.otto.mart.R
import com.otto.mart.model.localmodel.ppob.PpobPaymentMethod
import com.otto.mart.support.util.DataUtil
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.visible

/**
 * Created by macari on 3/7/18.
 */

class PpobPaymentMethodAdapter// Provide a suitable constructor (depends on the kind of dataset)
(private val mContext: Context, var mDataset: List<PpobPaymentMethod>) : androidx.recyclerview.widget.RecyclerView.Adapter<PpobPaymentMethodAdapter.ViewHolder>() {

    var mOnViewClickListener: OnViewClickListener? = null

    var mSelectedPosition: Int? = null
    var selectedRadioButton: RadioButton? = null
        set(value) {
            field?.isChecked = false
            field = value
        }

    class ViewHolder(v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v) {
        // each data item is just a string in this case
        var itemLayout: LinearLayout
        var tvName: TextView
        var tvBalance: TextView
        var radioButton: RadioButton
        var imgIcon: ImageView

        init {
            itemLayout = v.findViewById(R.id.itemLayout)
            tvName = v.findViewById(R.id.tvName)
            tvBalance = v.findViewById(R.id.tvBalance)
            imgIcon = v.findViewById(R.id.imgIcon)
            radioButton = v.findViewById(R.id.radioButton)
        }
    }

    // Create new views (invoked by the layout manager)x
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): PpobPaymentMethodAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_ppob_payment_method, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataset[position]

        if (item.balance != null) {
            holder.tvBalance.text = DataUtil.convertCurrency(item.balance)
            holder.tvBalance.visible()
        } else {
            holder.tvBalance.gone()
        }

        holder.tvName.text = item.name
        holder.imgIcon.background = ContextCompat.getDrawable(mContext, item.icon!!)

        holder.radioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                mSelectedPosition = position
                mOnViewClickListener?.onViewClickListener(item, position)
                selectedRadioButton = holder.radioButton
            }
        }

        holder.itemLayout.setOnClickListener {
            holder.radioButton.performClick()
        }

        if(item.selected!!) {
            holder.radioButton.performClick()
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
        fun onViewClickListener(item: PpobPaymentMethod, position: Int)
    }
}