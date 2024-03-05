package com.otto.mart.ui.activity.bogasari.catalog

import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.otto.mart.R
import com.otto.mart.model.APIModel.Response.bogasari.BogasariProduct
import com.otto.mart.support.util.DataUtil
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.onChange
import com.otto.mart.support.util.visible
import com.otto.mart.ui.Partials.RecyclerViewListener
import kotlinx.android.synthetic.main.item_product_bogasari.view.*
import kotlinx.android.synthetic.main.part_quantity.view.*

class CatalogBogasariAdapter(
        private val listener: RecyclerViewListener?,
        val fragmentListener: (BogasariProduct, Int) -> Unit
) : androidx.recyclerview.widget.RecyclerView.Adapter<CatalogBogasariAdapter.CatalogBogasariViewHolder>() {
    var products = mutableListOf<BogasariProduct>()
    var positionItem: Int = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogBogasariViewHolder {
        return CatalogBogasariViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_product_bogasari, parent, false))
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: CatalogBogasariViewHolder, position: Int) {
        holder.bind(products[position])
    }

    inner class CatalogBogasariViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        init {

            itemView.productPrice2.isEnabled = false

            itemView.productPrice2.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {

                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    s?.let {
                        if (it.isNotEmpty()) {
                            val price = DataUtil.getDigit(it.toString())
                            val newValue = DataUtil.convertCurrency(price)
                            itemView.productPrice2.removeTextChangedListener(this)
//                            itemView.productPrice2.setText(newValue.replace("Rp ", ""))
                            itemView.productPrice2.setSelection(itemView.productPrice2.length())
                            itemView.productPrice2.addTextChangedListener(this)

                            products[adapterPosition].price = price.toLong()
                            val qty = if (itemView.partQtyQuantity.text.isNotEmpty()) itemView.partQtyQuantity.text.toString().toInt() else 0
                            itemView.productSubtotal.text = DataUtil.convertCurrency(price.toDouble().times(qty))

                            if (qty > 0) {
                                itemView.productSubtotal.visible()
                                itemView.subTotalLabel.visible()
                            } else {
                                itemView.productSubtotal.gone()
                                itemView.subTotalLabel.gone()
                            }
                        } else {
                            products[adapterPosition].price = 0
                            itemView.productSubtotal.text = DataUtil.convertCurrency(0)

                            itemView.productSubtotal.gone()
                            itemView.subTotalLabel.gone()
                        }
                    }
                }
            })

            itemView.partQtyQuantity.onChange {
                if (it.isNotEmpty()) {
                    products[adapterPosition].quantity = it.toInt()

                    var price = if (itemView.productPrice2.text.isNotEmpty()) {
                        DataUtil.getDigit(itemView.productPrice2.text.toString()).toLong()
                    } else 0L

                    val subtotal = it.toInt() * price
                    itemView.productSubtotal.text = DataUtil.convertCurrency(subtotal)

                    if (subtotal > 0) {
                        itemView.productSubtotal.visible()
                        itemView.subTotalLabel.visible()
                    } else {
                        itemView.productSubtotal.gone()
                        itemView.subTotalLabel.gone()
                    }
                } else {
                    products[adapterPosition].quantity = 0
                    itemView.productSubtotal.text = DataUtil.convertCurrency(0)
                    itemView.productSubtotal.gone()
                    itemView.subTotalLabel.gone()
                }
            }

            itemView.partQtyMinus.setOnClickListener {
                var newQty = if (itemView.partQtyQuantity.text.isNotEmpty()) {
                    val qty = itemView.partQtyQuantity.text.toString().toInt()
                    if (qty - 1 > 0) {
                        (qty - 1).toString()
                    } else {
                        ""
                    }
                } else ""

                itemView.partQtyQuantity.setText(newQty)
            }

            itemView.partQtyPlus.setOnClickListener {
                var newQty =
                        if (itemView.partQtyQuantity.text.isNotEmpty()) {
                            val qty = itemView.partQtyQuantity.text.toString().toInt()
                            (qty.plus(1)).toString()
                        } else {
                            "1"
                        }

                itemView.partQtyQuantity.setText(newQty)
            }

            itemView.productSubtotal.onChange {
                var subtotal: Long = 0
                products.forEach {
                    val price = it.price ?: 0
                    val qty = it.quantity ?: 0
                    subtotal += price.times(qty)
                }
                listener?.let {
                    listener.onItemClick(0, 0, subtotal)
                }
            }
        }

        fun bind(item: BogasariProduct) {
            itemView.productName.text = item.name
            Glide.with(itemView.productImage).load(item.imageUrl).into(itemView.productImage)
            itemView.add_btn.setOnClickListener {
                fragmentListener(item, adapterPosition)
                positionItem = adapterPosition
            }
            itemView.amount_btn.setOnClickListener {
                fragmentListener(item, adapterPosition)
                positionItem = adapterPosition
            }
            if (products[adapterPosition].price != null) {
                if (products[adapterPosition].price!! >= 0L) {
                    itemView.add_btn.gone()
                    itemView.amount_btn.visible()
                    itemView.productPrice2.setText(DataUtil.convertCurrency(products[adapterPosition].price.toString()))

                    if (itemView.partQtyQuantity.text.isNotEmpty()) {
                        if (itemView.partQtyQuantity.text.toString().toInt() > 0) {
                            itemView.productSubtotal.visible()
                            itemView.subTotalLabel.visible()
                        } else {
                            itemView.productSubtotal.gone()
                            itemView.subTotalLabel.gone()
                        }
                    } else {
                        itemView.productSubtotal.gone()
                        itemView.subTotalLabel.gone()
                    }
                } else {
                    itemView.add_btn.visible()
                    itemView.amount_btn.gone()
                }
            } else {
                itemView.add_btn.visible()
                itemView.amount_btn.gone()

                itemView.productSubtotal.gone()
                itemView.subTotalLabel.gone()
            }
        }
    }

    fun setDataAmount(amount: Long) {
        if (positionItem != -1) {
            products[positionItem].price = amount
            notifyItemChanged(positionItem)
        } else {

        }
    }
}