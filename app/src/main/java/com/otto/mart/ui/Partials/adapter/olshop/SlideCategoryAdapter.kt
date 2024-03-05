package com.otto.mart.ui.Partials.adapter.olshop

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.otto.mart.R
import com.otto.mart.model.APIModel.Response.olshop.Category
import com.otto.mart.support.util.gone
import com.otto.mart.ui.Partials.RecyclerViewListener
import kotlinx.android.synthetic.main.item_slide_category.view.*

class SlideCategoryAdapter(val listener: RecyclerViewListener, val isSetBackgroundColor: Boolean) : androidx.recyclerview.widget.RecyclerView.Adapter<SlideCategoryAdapter.SlideCategoryVH>() {

    var categoryList: MutableList<Category>? = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var selectedItemBg: androidx.cardview.widget.CardView? = null
        set(value) {
            if (isSetBackgroundColor) {
                field?.let { it.setCardBackgroundColor(ContextCompat.getColor(it.context, R.color.color_white)) }
                value?.let { it.setCardBackgroundColor(ContextCompat.getColor(it.context, R.color.dark_sky_blue)) }
                field = value
            }
        }

    private var selectedItemText: TextView? = null
        set(value) {
            if (isSetBackgroundColor) {
                field?.let { it.setTextColor(ContextCompat.getColor(it.context, R.color.dark_grey_blue)) }
                value?.let { it.setTextColor(ContextCompat.getColor(it.context, R.color.color_white)) }
                field = value
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): SlideCategoryVH {
        return SlideCategoryVH(LayoutInflater.from(parent.context).inflate(R.layout.item_slide_category, parent, false))
    }

    override fun getItemCount(): Int = categoryList?.size ?: 0

    override fun onBindViewHolder(holder: SlideCategoryVH, position: Int) {
        holder.bind(categoryList!![position])
        holder.itemView.setOnClickListener {
            categoryList?.let {
                selectedItemBg = holder.itemView.itemParent
                selectedItemText = holder.itemView.categoryName
                listener.onItemClick(99, position, it[position])
            }
        }
    }

    class SlideCategoryVH(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bind(category: Category) {
            itemView.categoryName.text = category.name
            category.icon?.let { url ->
                if (url.isNotEmpty()) {
                    Glide.with(itemView.categoryIcon)
                            .load(url)
                            .listener(object : RequestListener<Drawable> {
                                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                    itemView.categoryIcon.gone()
                                    return false
                                }

                                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                    return false
                                }

                            })
                            .into(itemView.categoryIcon)
                } else itemView.categoryIcon.gone()
            }
        }
    }
}