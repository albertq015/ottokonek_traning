package id.ottopay.oasis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.otto.mart.model.APIModel.Response.grosir.DataResponseProductItem
import com.otto.mart.support.util.DataUtil
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.onChange
import com.otto.mart.support.util.visible
import com.otto.mart.ui.Partials.LoadMoreListener
import com.otto.mart.ui.Partials.RecyclerViewListener
import id.ottopay.oasis.R
import kotlinx.android.synthetic.main.item_grosir_catalog.view.*

class GrosirProductAdapter(val listener: RecyclerViewListener, val loadmore: LoadMoreListener, val recyclerView: androidx.recyclerview.widget.RecyclerView,var linearLayoutManager:LinearLayoutManager) : androidx.recyclerview.widget.RecyclerView.Adapter<GrosirProductAdapter.CatalogGrosirViewHolder>() {

    var products = mutableListOf<DataResponseProductItem>()
    var positionItem: Int = -1
    var visibleTreshold = 5
    var lastVisibleItem: Int = 0
    var totalItemCount: Int = 0
    var visibleItemCount:Int = 0
    var isLoading = false
    var productsCart = mutableListOf<DataResponseProductItem>()

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): CatalogGrosirViewHolder =
            CatalogGrosirViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_grosir_catalog, parent, false))

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(p0: CatalogGrosirViewHolder, p1: Int) {
        p0.bind(products[p1])
    }

    inner class CatalogGrosirViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        init {
            itemView.partQtyMinus.setOnClickListener {
                var multiply = products[adapterPosition].quantity_product
                var qty = 0
                var newQty = ""
                if (itemView.partQtyQuantity.text.toString().toInt() > 0) {
                    newQty = if (itemView.partQtyQuantity.text.isNotEmpty()) {
                        qty = itemView.partQtyQuantity.text.toString().toInt()
                        if (qty - multiply > 0) {
                            (qty - multiply).toString()
                        } else {
                            "0"
                        }
                    } else "0"

                    itemView.partQtyQuantity.setText(newQty)
                    var price = if (itemView.price_product.text.isNotEmpty()) {
                        DataUtil.FormattedCurrencyToDouble(products[adapterPosition].price.toBigDecimal().times(multiply.toBigDecimal()).toString())
                    } else 0.0

                    if(productsCart.size > 0) {
                        productsCart.forEach {
                            if(it.product_code.equals(products[adapterPosition].product_code)){
                                it.temp_qty = newQty.toInt()
                            }
                        }
                    }

                    listener.onItemClick(0, positionItem, price)
                }
            }

            itemView.partQtyPlus.setOnClickListener {
                var multiply = products[adapterPosition].quantity_product
                var newQty =
                        if (itemView.partQtyQuantity.text.isNotEmpty()) {
                            val qty = itemView.partQtyQuantity.text.toString().toLong()
                            (qty.plus(multiply)).toString()
                        } else {
                            multiply.toString()
                        }

                itemView.partQtyQuantity.setText(newQty)
                var price = if (itemView.price_product.text.isNotEmpty()) {
                    var amountPrice = products[adapterPosition].price.toBigDecimal().times(multiply.toBigDecimal())
                    DataUtil.FormattedCurrencyToDouble(amountPrice.toString())
                } else 0.0

                if(productsCart.size > 0) {
                    var isDuplicate = false
                    productsCart.forEach {
                        if(it.product_code.equals(products[adapterPosition].product_code)){
                            isDuplicate = true
                            it.temp_qty = newQty.toInt()
                        }
                    }
                    if(!isDuplicate){
                        products[adapterPosition].temp_qty = newQty.toInt()
                        productsCart.add(products[adapterPosition])
                    }
                }else{
                    productsCart.add(products[adapterPosition])
                }

                listener.onItemClick(1, positionItem, price)
            }

            itemView.partQtyQuantity.onChange {
                //products[adapterPosition].temp_qty = it.toInt()

                if (itemView.partQtyQuantity.text.toString().equals("0")) {
                    itemView.partQtyMinus.isEnabled = false
                } else {
                    itemView.partQtyMinus.isEnabled = true
                }
            }

            recyclerView.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    totalItemCount = linearLayoutManager.getItemCount()
                    visibleItemCount = linearLayoutManager.getChildCount()
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                    if (!isLoading && (visibleItemCount + lastVisibleItem) >= totalItemCount) {
                        loadmore?.onLoadMore()
                        isLoading = true
                    }
                }
            })

        }

        fun bind(item: DataResponseProductItem) {

            Glide.with(itemView.logo_product.context)
                    .load(item.photo).placeholder(com.otto.mart.R.drawable.icon_blank).error(com.otto.mart.R.drawable.icon_blank)
                    .into(itemView.logo_product)
            itemView.name_product.setText(item.name)
            itemView.desc_product.text = itemView.desc_product.context.getString(com.otto.mart.R.string.pembelian_kelipatan).replace("%$",products[adapterPosition].quantity_product.toString())
            itemView.partQtyQuantity.setText(item.temp_qty.toString())

            if(productsCart.size > 0){
                productsCart.forEach {
                    if(it.product_code.equals(item.product_code)){
                        itemView.partQtyQuantity.setText(it.temp_qty.toString())
                    }
                }
            }


            /*if (item.price.equals(item.real_price) || item.price.equals("0") || item.price.equals("")) {
                itemView.disc_cont.gone()
            } else {
                itemView.disc_cont.visible()
                itemView.normal_price.setText(DataUtil.convertCurrency(item.real_price))
                itemView.discount.setText(item.discount)
            }*/
            itemView.price_product.setText(DataUtil.InputDecimal(item.price.toFloat().toString()))

            if (item.stock.equals("Stock Tidak Tersedia", true)) {
                itemView.card.setOnClickListener {
                    null
                }
                itemView.card.setCardBackgroundColor(ContextCompat.getColor(itemView.context, com.otto.mart.R.color.grey))
                itemView.text.setText(itemView.context.getString(com.otto.mart.R.string.text_stock_unavailable))
                itemView.card.visible()
                itemView.button_nom.gone()

            } else {
                itemView.card.setOnClickListener {
                    positionItem = adapterPosition
                }
                itemView.card.gone()
                itemView.button_nom.visible()
                itemView.card.setCardBackgroundColor(ContextCompat.getColor(itemView.context, com.otto.mart.R.color.dark_sky_blue))
                //itemView.text.setText("Beli Sekarang")
                itemView.partQtyQuantity.onChange {
                    if (it.isNotEmpty()) {
                        //listener.onItemClick(itemView.partQtyQuantity.text.toString().toInt(), positionItem, item)
                        products.get(adapterPosition).temp_qty = Integer.valueOf(it)
                    }
                }

            }
        }
    }
}

