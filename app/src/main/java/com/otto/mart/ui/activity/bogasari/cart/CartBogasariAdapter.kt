package com.otto.mart.ui.activity.bogasari.cart

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.otto.mart.R
import com.otto.mart.model.APIModel.Request.bogasari.BogasariInquiryProduct
import com.otto.mart.support.util.DataUtil
import kotlinx.android.synthetic.main.item_cart_bogasari.view.*
import java.util.*

class CartBogasariAdapter(val collectedProduct: ArrayList<BogasariInquiryProduct>) : androidx.recyclerview.widget.RecyclerView.Adapter<CartBogasariAdapter.CartBogasariViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartBogasariViewHolder {
        return CartBogasariViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_cart_bogasari, parent, false))
    }

    override fun getItemCount(): Int = collectedProduct.size


    override fun onBindViewHolder(holder: CartBogasariViewHolder, position: Int) {
        holder.bind(collectedProduct[position])
    }

    inner class CartBogasariViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        fun bind(bogasariProduct: BogasariInquiryProduct) {
            val qty = if (bogasariProduct.itemQuantity ?: 0 > 0) "${bogasariProduct.itemQuantity}X " else ""
            val name = "$qty${bogasariProduct.itemName}"
            val subtotal = bogasariProduct.itemUnitPrice?.times(
                    if (bogasariProduct.itemQuantity ?: 0 < 1) 1
                    else bogasariProduct.itemQuantity ?: 1
            )

            itemView.productName.text = name
            itemView.productPrice.text = DataUtil.convertCurrency(bogasariProduct.itemUnitPrice)
            itemView.productSubtotal.text = DataUtil.convertCurrency(subtotal)
        }
    }
}