package id.ottopay.oasis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.otto.mart.model.APIModel.Response.grosir.DataResponseItemList
import com.otto.mart.ui.Partials.RecyclerViewListener
import id.ottopay.oasis.R
import kotlinx.android.synthetic.main.item_categori.view.*

class GrosirCategoryList(val listener : RecyclerViewListener ) : androidx.recyclerview.widget.RecyclerView.Adapter<GrosirCategoryList.CategoryGrosirList>(){

    var categories: DataResponseItemList? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var item_now = 0

    override fun onBindViewHolder(p0: CategoryGrosirList, p1: Int) {
        categories?.let { categories ->
            //if(categories.list_category[0].name.equals("all",true)) {
                p0.itemView.item.setText(p0.itemView.context.getString(com.otto.mart.R.string.text_all_product))
            //}

            p0.itemView.setOnClickListener {
                listener.onItemClick(categories.id, p1, "all")
                item_now = p1
                notifyDataSetChanged()
            }
            if(item_now == p1){
                p0.itemView.item_cont.setBackgroundResource(com.otto.mart.R.color.white)
            }else{
                p0.itemView.item_cont.setBackgroundResource(com.otto.mart.R.color.grey_soft)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): CategoryGrosirList =
            CategoryGrosirList(LayoutInflater.from(parent.context).inflate(R.layout.item_categori, parent, false))

    override fun getItemCount(): Int = 1


    class CategoryGrosirList(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView)
}