package com.otto.mart.ui.Partials.adapter.setting

import android.content.Context
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.otto.mart.R
import com.otto.mart.model.localmodel.profile.ProfileMenu
import kotlinx.android.synthetic.main.item_setting_menu.view.*

/**
 * Created by macari on 3/7/18.
 */

class SettingMenuAdapter(private val mContext: Context, var mDataset: List<ProfileMenu>) : RecyclerView.Adapter<SettingMenuAdapter.ViewHolder>() {

    var mOnViewClickListener: OnViewClickListener? = null

    class ViewHolder(val v: View) : RecyclerView.ViewHolder(v)

    // Create new views (invoked by the layout manager)x
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_setting_menu, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataset[position]

        holder.v.tvMenu.text = item.name

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
        fun onViewClickListener(data: ProfileMenu, position: Int)
    }
}