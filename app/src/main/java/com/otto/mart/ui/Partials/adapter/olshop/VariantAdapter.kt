package com.otto.mart.ui.Partials.adapter.olshop

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.otto.mart.R
import com.otto.mart.model.APIModel.Misc.olshop.Variant
import kotlinx.android.synthetic.main.item_ppob_komisi.view.*

class VariantAdapter(private val mDataset: List<Variant>?, private val viewPos: Int) : androidx.recyclerview.widget.RecyclerView.Adapter<VariantAdapter.ViewHolder>() {

    var mOnViewClickListener: OnViewClickListener? = null
    private var cardState: androidx.cardview.widget.CardView? = null
    private var textViewState: View? = null

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): VariantAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_ppob_komisi, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mDataset?.let {
            holder.bind(mDataset[position])

            mOnViewClickListener?.let { listener ->
                holder.v.setOnClickListener { viewHolder ->
                    listener.onViewClickListener(mDataset[position], position, viewPos)

                    cardState?.context?.let {
                        setColor(cardState, it, R.color.white)
                    }
                    cardState = viewHolder.card

                    textViewState?.context?.let {
                        setColor(textViewState, it, R.color.blue_grey)
                    }
                    textViewState = viewHolder.tvKomisi

                    setColor(viewHolder.card, holder.v.context, R.color.dark_sky_blue)
                    setColor(viewHolder.tvKomisi, holder.v.context, R.color.white)
                }
            }

            if (position == 0) holder.v.performClick()
        }
    }

    private fun setColor(view: View?, context: Context, color: Int) {
        view?.let { view ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (view is androidx.cardview.widget.CardView) {
                    view.setCardBackgroundColor(context.getColor(color))
                } else if (view is TextView) {
                    view.setTextColor(context.getColor(color))
                }
            } else {
                if (view is androidx.cardview.widget.CardView) {
                    view.setCardBackgroundColor(context.resources.getColor(color))
                } else if (view is TextView) {
                    view.setTextColor(context.resources.getColor(color))
                }
            }

        }
    }

    override fun getItemCount(): Int = mDataset?.size ?: 0

    class ViewHolder(val v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v) {
        @SuppressLint("SetTextI18n")
        fun bind(variant: Variant) {
            var title = variant.value
            if (title.length > 1) {
                title = variant.value.substring(0, 1).toUpperCase() + variant.value.substring(1)
            }
            v.tvKomisi.text = title
        }
    }

    interface OnViewClickListener {
        fun onViewClickListener(item: Variant, position: Int, viewPos: Int)
    }
}