package id.ottopay.oasis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.otto.mart.model.APIModel.Response.grosir.DataResponseCheckStatus
import com.otto.mart.support.util.DataUtil
import com.otto.mart.support.util.DateUtil
import com.otto.mart.ui.Partials.RecyclerViewListener
import id.ottopay.oasis.R
import kotlinx.android.synthetic.main.item_history_grosir.view.*

class GrosirListCheckOut(val listener : RecyclerViewListener) : androidx.recyclerview.widget.RecyclerView.Adapter<GrosirListCheckOut.CatalogGrosirViewHolder>(){

    var products = DataResponseCheckStatus()

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): CatalogGrosirViewHolder =
            CatalogGrosirViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_history_grosir, parent, false))

    override fun getItemCount(): Int = products.data.size

    override fun onBindViewHolder(p0: CatalogGrosirViewHolder, p1: Int) {p0.bind(p1,products)}
    inner class CatalogGrosirViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bind(positionItem : Int,item: DataResponseCheckStatus) {
            itemView.status.text = item.data.get(positionItem).status_order
            itemView.no_pemesanan.text = item.data.get(positionItem).order_no
            itemView.tanggal_pemesanan.text = DateUtil.formatDate(item.data.get(positionItem).order_date , "dd MMMM yyyy", "yyyy-MM-dd")
            itemView.total_pembayaran.text = DataUtil.convertCurrency(item.data.get(positionItem).total_amount+item.data.get(positionItem).shipment.courier_cost)
            if(item.data.get(positionItem).status_order.equals("Rejected",true)){
                itemView.status_pembayaran.setTextColor(ContextCompat.getColor(itemView.context,com.otto.mart.R.color.cherry_red))
            }else{
                itemView.status_pembayaran.setTextColor(ContextCompat.getColor(itemView.context,com.otto.mart.R.color.apple_green))
            }
            itemView.status_pembayaran.text = item.data.get(positionItem).paymentStatus

                itemView.card.setOnClickListener {
                    listener.onItemClick(0, positionItem, item.data.get(positionItem))
            }
        }

    }

}