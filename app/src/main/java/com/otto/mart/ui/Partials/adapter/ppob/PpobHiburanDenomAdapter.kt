package com.otto.mart.ui.Partials.adapter.ppob

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import cl.jesualex.stooltip.Position
import cl.jesualex.stooltip.Tooltip
import com.otto.mart.R
import com.otto.mart.model.APIModel.Misc.OttoagDenomModel
import com.otto.mart.support.util.DataUtil
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.visible
import kotlinx.android.synthetic.main.item_ppob_denom.view.itemLayout
import kotlinx.android.synthetic.main.item_ppob_denom.view.tvCashback
import kotlinx.android.synthetic.main.item_ppob_denom.view.tvName
import kotlinx.android.synthetic.main.item_ppob_denom.view.tvPrice
import kotlinx.android.synthetic.main.item_ppob_hiburan_denom.view.*

/**
 * Created by macari on 3/7/18.
 */

class PpobHiburanDenomAdapter// Provide a suitable constructor (depends on the kind of dataset)
(private val mContext: Context, var mDataset: List<OttoagDenomModel>) : androidx.recyclerview.widget.RecyclerView.Adapter<PpobHiburanDenomAdapter.ViewHolder>() {

    lateinit var itemSelected: (OttoagDenomModel, Int) -> Unit

    class ViewHolder(val v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v)

    // Create new views (invoked by the layout manager)x
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): PpobHiburanDenomAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_ppob_hiburan_denom, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataset[position]

        if (item.isSelected) {
            holder.v.itemLayout.background = ContextCompat.getDrawable(mContext, R.drawable.button_primary_selector)
            holder.v.tvName.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            holder.v.tvPrice.setTextColor(ContextCompat.getColor(mContext, R.color.white))
        } else {
            holder.v.itemLayout.background = ContextCompat.getDrawable(mContext, R.drawable.button_white_grey_selector)
            holder.v.tvName.setTextColor(ContextCompat.getColor(mContext, R.color.text_blue))
            holder.v.tvPrice.setTextColor(ContextCompat.getColor(mContext, R.color.text_blue))
        }

        if (item.isDisabled) {
            holder.v.itemLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.very_light_grey))
            holder.v.tvName.setTextColor(ContextCompat.getColor(mContext, R.color.text_brown))
            holder.v.tvPrice.setTextColor(ContextCompat.getColor(mContext, R.color.text_brown))
        }

        holder.v.imgInfo.setOnClickListener {
            setupToolTip(item.description, holder.v.imgInfo)
        }

        if (item.cashback_omzet != null) {
            if (item.cashback_omzet != 0L) {
                holder.v.tvCashback.text = "Cashback: " + DataUtil.convertCurrency(item.cashback_omzet)
                holder.v.tvCashback.visible()
            } else {
                holder.v.tvCashback.gone()
            }
        }

        holder.v.tvName.text = item.product_name
        holder.v.tvPrice.text = DataUtil.convertCurrency(item.price.toString())

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

    private fun setupToolTip(message: String, view: ImageView) {
        val tooltip = Tooltip.on(view)
                .text(message)
                .color(mContext.resources.getColor(R.color.blue_grey))
                .textSize(12f)
                .border(Color.BLACK, 1f)
                .clickToHide(true)
                .corner(5)
                .position(Position.BOTTOM)
                .show(3000)
    }
}