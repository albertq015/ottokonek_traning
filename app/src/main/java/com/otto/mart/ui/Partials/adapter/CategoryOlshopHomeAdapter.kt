package com.otto.mart.ui.Partials.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.otto.mart.BuildConfig
import com.otto.mart.R
import com.otto.mart.model.APIModel.Response.olshop.Category
import com.otto.mart.ui.Partials.RecyclerViewListener
import kotlinx.android.synthetic.main.item_image_only.view.*

class CategoryOlshopHomeAdapter(val listener: RecyclerViewListener) : androidx.recyclerview.widget.RecyclerView.Adapter<CategoryOlshopHomeAdapter.CategoryOlshopVH>() {

    var categories: MutableList<Category>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): CategoryOlshopVH =
            CategoryOlshopVH(LayoutInflater.from(parent.context).inflate(R.layout.item_image_only, parent, false))

    override fun getItemCount(): Int = categories?.size ?: 0

    override fun onBindViewHolder(holder: CategoryOlshopVH, position: Int) {
        categories?.let { category ->
            Glide.with(holder.itemView.image.context)
                    .load(BuildConfig.RESOURCE + category[position].icon)
                    .into(holder.itemView.image)

            holder.itemView.setOnClickListener { listener.onItemClick(0, position, category[position]) }
        }

    }

    class CategoryOlshopVH(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView)
}