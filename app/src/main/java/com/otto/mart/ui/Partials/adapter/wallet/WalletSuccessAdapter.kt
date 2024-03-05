package com.otto.mart.ui.Partials.adapter.wallet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.otto.mart.R
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel
import kotlinx.android.synthetic.main.cw_textview_p.view.*
import java.util.*

class WalletSuccessAdapter(private val keyValue: List<WidgetBuilderModel>?) : RecyclerView.Adapter<WalletSuccessAdapter.VH>() {
    var listener: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
            VH(LayoutInflater.from(parent.context).inflate(R.layout.cw_textview_horizontal_e, parent, false))

    override fun getItemCount(): Int = keyValue?.size ?: 0

    override fun onBindViewHolder(holder: VH, position: Int) {
        keyValue?.let { holder.bind(it[position]) }
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(widgetBuilderModel: WidgetBuilderModel) {
            itemView.title.text = widgetBuilderModel.key
            itemView.contentContainer.text = widgetBuilderModel.value

            if (widgetBuilderModel.key.toLowerCase(Locale.US).equals("no resi").or(
                            widgetBuilderModel.key.toLowerCase(Locale.US).equals("no. resi")).or(
                            widgetBuilderModel.key.toLowerCase(Locale.US).equals("nomor resi"))) {

                listener?.invoke(widgetBuilderModel.value)
            }
        }
    }
}