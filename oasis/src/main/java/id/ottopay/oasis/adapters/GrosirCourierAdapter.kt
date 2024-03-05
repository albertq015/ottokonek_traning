package id.ottopay.oasis.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.otto.mart.model.APIModel.Response.grosir.OasisListCourier
import com.otto.mart.model.APIModel.Response.grosir.ServiceCostShipment
import com.otto.mart.support.util.DataUtil
import id.ottopay.oasis.R

class GrosirCourierAdapter (private val mContext:Context, var mDataset:List<OasisListCourier>) : androidx.recyclerview.widget.RecyclerView.Adapter<GrosirCourierAdapter.ViewHolder>(){

    var mOnViewClickListener: OnViewClickListener? = null

    var mSelectedPosition: Int? = null

    class ViewHolder(v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v) {

        var tvName: TextView
        var tvCost: TextView
        var itemLayout :LinearLayout
        var imageKurir :ImageView

        init {
            tvName = v.findViewById(R.id.text_courier)
            tvCost = v.findViewById(R.id.cost_courier)
            itemLayout =v.findViewById(R.id.layout)
            imageKurir = v.findViewById(R.id.image_kurir)
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context)
                .inflate(R.layout.grosir_courier_item, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mDataset.size
    }

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
        val item = mDataset[p1]

        holder.tvName.text = item.name
        holder.tvCost.text = DataUtil.convertCurrency(item.price)
        if(item.image_path!!.isNotEmpty()){
            Glide.with(holder.imageKurir.context).load(item.image_path).into(holder.imageKurir)
        }

        holder.itemLayout.setOnClickListener {
            mSelectedPosition = p1
            mOnViewClickListener?.onViewClickListener(item, p1)
        }
    }

    fun setmOnViewClickListener(mOnViewClickListener: OnViewClickListener) {
        this.mOnViewClickListener = mOnViewClickListener
    }

    interface OnViewClickListener {
        fun onViewClickListener(item: OasisListCourier, position: Int)
    }
}