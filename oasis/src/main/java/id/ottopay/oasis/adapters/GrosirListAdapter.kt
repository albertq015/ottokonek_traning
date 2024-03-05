package id.ottopay.oasis.adapters;

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.otto.mart.model.APIModel.Response.grosir.GrosirListSupplierResponse
import com.otto.mart.ui.Partials.RecyclerViewListener
import id.ottopay.oasis.R
import kotlinx.android.synthetic.main.item_grosir_supplier.view.*

class GrosirListAdapter(val listener : RecyclerViewListener, val activity : Activity) : androidx.recyclerview.widget.RecyclerView.Adapter<GrosirListAdapter.CategoryGrosirList>(){

    var categories: GrosirListSupplierResponse? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onBindViewHolder(p0: CategoryGrosirList, p1: Int) {
        categories?.let { categories ->
            Glide.with(p0.itemView.logo_grosir.context)
                    .load(categories.data.get(p1).logo_path).error(com.otto
                            .mart.R.drawable.icon_blank).placeholder(com.otto.mart.R.drawable.icon_blank)
                    .into(p0.itemView.logo_grosir)

            p0.itemView.nama_toko.setText(categories.data.get(p1).name)

            if(categories.data.get(p1).description!=null &&categories.data.get(p1).description!= ""){
                p0.itemView.desc_toko.text = categories.data.get(p1).description
                p0.itemView.desc_toko.visibility = View.VISIBLE
            }else {
                p0.itemView.desc_toko.visibility = View.GONE
            }

            p0.itemView.setOnClickListener { listener.onItemClick(0, p1, categories.data.get(p1)) }

            /*if(categories.data.get(p1).total_product>0) {
                p0.itemView.view_container_list.visibility = View.VISIBLE

            }else{
                p0.itemView.view_container_list.visibility = View.GONE
            }*/
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): CategoryGrosirList =
            CategoryGrosirList(LayoutInflater.from(parent.context).inflate(R.layout.item_grosir_supplier, parent, false))

    override fun getItemCount(): Int = categories?.data?.size?: 0


    class CategoryGrosirList(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView)
}