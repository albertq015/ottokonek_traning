package com.otto.mart.ui.Partials.adapter.profile

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.otto.mart.R
import com.otto.mart.model.localmodel.profile.ProfileMenu
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.px
import com.otto.mart.support.util.visible
import kotlinx.android.synthetic.main.item_profile_menu.view.*
import org.koin.ext.getScopeName

/**
 * Created by macari on 3/7/18.
 */

class ProfileMenuAdapter
(private val mContext: Context, var mDataset: List<ProfileMenu>) : androidx.recyclerview.widget.RecyclerView.Adapter<ProfileMenuAdapter.ViewHolder>() {

    var mOnViewClickListener: OnViewClickListener? = null

    class ViewHolder(val v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v)

    // Create new views (invoked by the layout manager)x
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileMenuAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_profile_menu, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataset[position]

        if (mDataset[position].name.equals("Logout")) {
            holder.v.tvMenu.setTextColor(ContextCompat.getColor(mContext, R.color.dark_sky_blue))
            holder.v.menuLayout.gone()
            holder.v.logoutLayout.visible()
        } else {
            holder.v.tvMenu.setTextColor(ContextCompat.getColor(mContext, R.color.text_black))
            holder.v.menuLayout.visible()
            holder.v.logoutLayout.gone()
        }

        holder.v.tvMenu.text = item.name

        holder.v.itemLayout.setOnClickListener {
            mOnViewClickListener?.onViewClickListener(item, position)
        }

        if (SessionManager.getMerchantTheme() != null && SessionManager.getMerchantTheme().themeColor != null) {
            if (SessionManager.getMerchantTheme().themeColor.contains("#")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    var themeColor = SessionManager.getMerchantTheme().themeColor
                    val gradBackground = GradientDrawable()
                    gradBackground.cornerRadius = 8.px().toFloat()
                    gradBackground.setStroke(1.px(), Color.parseColor(themeColor))
                    holder.v.textView50.setTextColor(Color.parseColor(themeColor))
                    DrawableCompat.setTint(holder.v.imageView.drawable, Color.parseColor(themeColor))
                    holder.v.logoutLayout.background = gradBackground
                }
            }
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