package id.ottopay.oasis.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.otto.mart.model.APIModel.Response.grosir.OasisItemCategoryResponse
import id.ottopay.oasis.R

class OasisCategoryDialogAdapter (private val mContext: Context, var mDataset:List<OasisItemCategoryResponse>) : androidx.recyclerview.widget.RecyclerView.Adapter<OasisCategoryDialogAdapter.ViewHolder>() {

    var mOnViewClickListener: OnViewClickListener? = null

    var mSelectedPosition: Int? = null

    class ViewHolder(v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v) {

        var tvName: TextView
        var itemLayout: LinearLayout
        var tvProduct :TextView

        init {
            tvName = v.findViewById(R.id.category_name)
            itemLayout = v.findViewById(R.id.container_card)
            tvProduct = v.findViewById(R.id.category_product)
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context)
                .inflate(R.layout.oasis_category_item, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mDataset.size
    }

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
        val item = mDataset[p1]

        holder.tvName.text = item.name
        holder.tvProduct.text = item.total_product.toString() +" "+ mContext.getString(com.otto.mart.R.string.product)

        holder.itemLayout.setOnClickListener {
            mSelectedPosition = p1
            mOnViewClickListener?.onViewClickListener(item, p1)
        }
    }


    fun setmOnViewClickListener(mOnViewClickListener: OnViewClickListener) {
        this.mOnViewClickListener = mOnViewClickListener
    }

    interface OnViewClickListener {
        fun onViewClickListener(item: OasisItemCategoryResponse, position: Int)
    }
}