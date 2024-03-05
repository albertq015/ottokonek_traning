package com.otto.mart.ui.Partials.adapter.setting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.otto.mart.R
import com.otto.mart.model.localmodel.setting.LanguageOption
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.visible
import kotlinx.android.synthetic.main.item_language_option.view.*

/**
 * Created by macari on 3/7/18.
 */

class LanguageOptionAdapter(private val mContext: Context, var mDataset: List<LanguageOption>) : RecyclerView.Adapter<LanguageOptionAdapter.ViewHolder>() {

    var mOnViewClickListener: OnViewClickListener? = null

    class ViewHolder(val v: View) : RecyclerView.ViewHolder(v)

    // Create new views (invoked by the layout manager)x
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_language_option, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataset[position]

        holder.v.ivSelected.gone()

        holder.v.tvName.text = item.name

        if (SessionManager.getLanguageCode() != null) {
            if (SessionManager.getLanguageCode().equals(item.code)) {
                holder.v.ivSelected.visible()
            } else {
                holder.v.ivSelected.gone()
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
        fun onViewClickListener(data: LanguageOption, position: Int)
    }
}