package com.otto.mart.ui.Partials.adapter.payment

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.otto.mart.R
import com.otto.mart.model.localmodel.payment.TopUpGuide
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.visible
import kotlinx.android.synthetic.main.item_omzet_filter.view.itemLayout
import kotlinx.android.synthetic.main.item_topup_guide.view.*

/**
 * Created by macari on 3/7/18.
 */

class TopUpGuideAdapter// Provide a suitable constructor (depends on the kind of dataset)
(private val mContext: Context, var mDataset: List<TopUpGuide>) : androidx.recyclerview.widget.RecyclerView.Adapter<TopUpGuideAdapter.ViewHolder>() {

    lateinit var itemSelected: (TopUpGuide, Int) -> Unit

    class ViewHolder(val v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        var layout = R.layout.item_topup_guide

        val view = LayoutInflater.from(parent.context)
                .inflate(layout, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataset[position]

        holder.v.tvTitle.text = item.title

        if (item.isShow) {
            holder.v.imgArrow.rotation = 0f

            val layoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            holder.v.stepContainer.removeAllViews()

            for (step in item.stepList) {
                val view: View = layoutInflater.inflate(R.layout.item_topup_guide_child, null)
                val tvStep = view.findViewById<View>(R.id.tvStep) as TextView
                val itemLayoutChild = view.findViewById<View>(R.id.itemLayoutChild) as LinearLayout

                tvStep.text = step

                itemLayoutChild.setOnClickListener {
                    if (::itemSelected.isInitialized) {
                        itemSelected(item, position)
                    }
                }

                tvStep.setOnClickListener {
                    if (::itemSelected.isInitialized) {
                        itemSelected(item, position)
                    }
                }

                holder.v.stepContainer.addView(view)
            }

            holder.v.stepContainer.visible()
        } else {
            holder.v.imgArrow.rotation = 180f
            holder.v.stepContainer.gone()
        }

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