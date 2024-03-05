package com.otto.mart.ui.Partials.adapter.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.otto.mart.R
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel

/**
 * Created by macari on 3/7/18.
 */

class ProfileInfoAdapter
(private val mContext: Context, var mDataset: List<WidgetBuilderModel>,
    val itemClicked: (item: WidgetBuilderModel, position: Int) -> Unit, val btnEditClicked: () -> Unit) : androidx.recyclerview.widget.RecyclerView.Adapter<ProfileInfoAdapter.ViewHolder>() {

    class ViewHolder(v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v) {
        // each data item is just a string in this case
        var itemLayout: LinearLayout
        var tvKey: TextView
        var tvValue: TextView
        var btnEdit: Button

        init {
            itemLayout = v.findViewById(R.id.itemLayout)
            tvKey = v.findViewById(R.id.tvKey)
            tvValue = v.findViewById(R.id.tvValue)
            btnEdit = v.findViewById(R.id.btnEdit)
        }
    }

    // Create new views (invoked by the layout manager)x
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ProfileInfoAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_profile_info, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataset[position]

//        if (position == 0) holder.btnEdit.visible() else holder.btnEdit.gone()

        holder.tvKey.text = item.key
        holder.tvValue.text = item.value

//        holder.btnEdit.setOnClickListener {
//            btnEditClicked()
//        }

        holder.itemLayout.setOnClickListener {
            itemClicked(item, position)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return mDataset.size
    }
}