package id.ottopay.oasis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.otto.mart.model.APIModel.Request.grosir.GrosirItem
import com.otto.mart.support.util.DataUtil
import id.ottopay.oasis.R
import kotlinx.android.synthetic.main.item_cart_grosir.view.*

class GrosirChartAdapter(val collectedProduct: ArrayList<GrosirItem>) : androidx.recyclerview.widget.RecyclerView.Adapter<GrosirChartAdapter.CartBogasariViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartBogasariViewHolder {
        return CartBogasariViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_cart_grosir, parent, false))
    }

    override fun getItemCount(): Int = collectedProduct.size


    override fun onBindViewHolder(holder: CartBogasariViewHolder, position: Int) {
        holder.bind(collectedProduct[position])
    }

    inner class CartBogasariViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        fun bind(bogasariProduct: GrosirItem) {
            val weight = bogasariProduct.weight.times(bogasariProduct.quantity.toLong())
            val qty = if (bogasariProduct.quantity.toLong() ?: 0 > 0) "${bogasariProduct.quantity}"+" pcs ("+weight+" gr)" else ""
            val priceQty = "$qty${DataUtil.convertCurrency(bogasariProduct.real_price)}"
            val subtotal = bogasariProduct.real_price.toBigDecimal()

            Glide.with(itemView.imageView5.context)
                    .load(bogasariProduct.url).error(com.otto.mart.R.drawable.icon_blank).placeholder(com.otto.mart.R.drawable.icon_blank)
                    .into(itemView.imageView5)

            itemView.productName.text = bogasariProduct.name
            itemView.productPrice.text = qty
            itemView.productSubtotal.text = DataUtil.InputDecimal(subtotal.toString())
        }
    }
}