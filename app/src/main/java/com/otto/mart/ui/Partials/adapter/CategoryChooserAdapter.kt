package com.otto.mart.ui.Partials.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.otto.mart.R
import com.otto.mart.model.APIModel.Response.olshop.Category
import kotlinx.android.synthetic.main.item_category_chooser.view.*

class CategoryChooserAdapter(val listener: (Int, Category) -> Unit) : androidx.recyclerview.widget.RecyclerView.Adapter<CategoryChooserAdapter.SimpleTextVH>() {
    var categories: MutableList<Category>? = null
        set(value) {
            field = value
            stateIndexList.clear()
            notifyDataSetChanged()
        }

    private val stateIndexList= mutableListOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleTextVH =
            SimpleTextVH(LayoutInflater.from(parent.context).inflate(R.layout.item_category_chooser, parent, false))

    override fun getItemCount(): Int = categories?.size ?: 0

    override fun onBindViewHolder(holder: SimpleTextVH, position: Int) {
        categories?.let { holder.bind(it[position]) }
    }

    inner class SimpleTextVH(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bind(category: Category) {
            itemView.categoryName.text = category.name
            itemView.setOnClickListener { listener(adapterPosition,category) }
        }
    }
}