package id.ottopay.oasis.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.otto.mart.model.APIModel.Response.grosir.HistoryOasisOrderItem
import com.otto.mart.support.util.DataUtil
import com.otto.mart.support.util.DateUtil
import id.ottopay.oasis.R

class HistoryOasisFragmentAdapter (private val mContext :Context, var mDataset:List<HistoryOasisOrderItem>) : androidx.recyclerview.widget.RecyclerView.Adapter<HistoryOasisFragmentAdapter.ViewHolder>(){

    var mOnViewClickListener: OnViewClickListener? = null
    var mOnConfirmClickListener: OnConfirmClickListener? = null

    var mSelectedPosition: Int? = null

    class ViewHolder(v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v) {

        var tvType: TextView
        var merchantImage : ImageView
        var merchantName : TextView
        var orderDate : TextView
        var no_resi : TextView
        var no_pesanan : TextView
        var status_pemesanan : TextView
        var total_pembayaran : TextView
        var text_button : TextView
        var container_card : CardView
        var button_card : CardView



        init {
            tvType = v.findViewById(R.id.type_order)
            merchantImage =v.findViewById(R.id.merchant_image)
            merchantName = v.findViewById(R.id.merchant_name)
            orderDate = v.findViewById(R.id.order_date)
            no_resi = v.findViewById(R.id.no_resi)
            no_pesanan = v.findViewById(R.id.no_pesanan)
            status_pemesanan = v.findViewById(R.id.status_pemesanan)
            total_pembayaran = v.findViewById(R.id.total_pembayaran)
            text_button = v.findViewById(R.id.text_button)
            container_card = v.findViewById(R.id.container_card)
            button_card = v.findViewById(R.id.button_card)
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context)
                .inflate(R.layout.history_oasis_item, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mDataset.size
    }

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
        val item = mDataset[p1]

        if(item.payment_status.equals("Paid",true)){
            holder.status_pemesanan.setTextColor(mContext.resources.getColor(com.otto.mart.R.color.green_dark_4))
        }else{
            holder.status_pemesanan.setTextColor(mContext.resources.getColor(com.otto.mart.R.color.red_dark_oasis))
        }


        if(item.status_order.equals("Order Delivered",true)) {
            holder.button_card.visibility = View.VISIBLE
            holder.tvType.background = mContext.resources.getDrawable(com.otto.mart.R.drawable.corner_top_drawable)
            holder.tvType.setTextColor(mContext.resources.getColor(com.otto.mart.R.color.green_dark_4))
        }/* else if(item.status_order.equals("pesanan baru",true)){
            holder.button_card.visibility = View.GONE
            holder.tvType.background = mContext.resources.getDrawable(com.otto.mart.R.drawable.corner_top_drawable_grey)
            holder.tvType.setTextColor(mContext.resources.getColor(com.otto.mart.R.color.white_dark_oasis))
        }*/ else if(item.status_order.equals("Order In Transit",true)){
            holder.button_card.visibility = View.GONE
            holder.tvType.background = mContext.resources.getDrawable(com.otto.mart.R.drawable.corner_top_drawable_orange)
            holder.tvType.setTextColor(mContext.resources.getColor(com.otto.mart.R.color.orange_dark_oasis))
        } else if(item.status_order.equals("Order Completed",true)){
            holder.button_card.visibility = View.GONE
            holder.tvType.background = mContext.resources.getDrawable(com.otto.mart.R.drawable.corner_top_drawable_green)
            holder.tvType.setTextColor(mContext.resources.getColor(com.otto.mart.R.color.dark_blue_green))
        } else if(item.status_order.equals("Order Processed",true) ) {
            holder.button_card.visibility = View.GONE
            holder.tvType.background = mContext.resources.getDrawable(com.otto.mart.R.drawable.corner_top_drawable)
            holder.tvType.setTextColor(mContext.resources.getColor(com.otto.mart.R.color.blue_dark_oasis))
        }else if(item.status_order.equals("Order Cancelled",true) ) {
            holder.button_card.visibility = View.GONE
            holder.tvType.background = mContext.resources.getDrawable(com.otto.mart.R.drawable.corner_top_drawable_red)
            holder.tvType.setTextColor(mContext.resources.getColor(com.otto.mart.R.color.red_dark_oasis))
        }
        else {
            holder.button_card.visibility = View.GONE
            holder.tvType.background = mContext.resources.getDrawable(com.otto.mart.R.drawable.corner_top_drawable_orange)
            holder.tvType.setTextColor(mContext.resources.getColor(com.otto.mart.R.color.orange_dark_oasis))
        }

        holder.tvType.text = item.status_order
        holder.total_pembayaran.text = DataUtil.InputDecimal((item.total_amount!!.toBigDecimal().plus(item.courier_cost!!.toBigDecimal())).toString())

//        if (item.payment_method?.name.equals("Pay at Delivery")) {
//            holder.button_card.visibility = View.GONE
//        }
        holder.button_card.setOnClickListener {
            mOnConfirmClickListener?.onConfirmClickListener(p1)
        }
        holder.container_card.setOnClickListener {
            //mSelectedPosition = p1
            mOnViewClickListener?.onViewClickListener(item, p1)
        }
        holder.status_pemesanan.text = item.payment_status
        holder.no_pesanan.text = item.order_no
        holder.no_resi.visibility = View.GONE
        holder.orderDate.text = DateUtil.formatDate(item.order_date,"dd-MMMM-yyyy","yyyy-MM-dd")
        holder.merchantName.text = item.supplier_name
    }



    fun setmOnViewClickListener(mOnViewClickListener: OnViewClickListener) {
        this.mOnViewClickListener = mOnViewClickListener
    }

    fun setmOnConfirmClickListener(mOnConfirmClickListener: OnConfirmClickListener){
        this.mOnConfirmClickListener = mOnConfirmClickListener
    }

    interface OnViewClickListener {
        fun onViewClickListener(item: HistoryOasisOrderItem, position: Int)
    }

    interface OnConfirmClickListener {
        fun onConfirmClickListener(position: Int)
    }
}