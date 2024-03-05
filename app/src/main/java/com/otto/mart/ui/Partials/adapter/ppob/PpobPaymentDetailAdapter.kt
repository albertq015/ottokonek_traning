package com.otto.mart.ui.Partials.adapter.ppob

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.otto.mart.R
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel
import com.otto.mart.model.localmodel.omzet.OmzetFilter
import com.otto.mart.support.util.encryption.AesEncryption
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.showToast
import com.otto.mart.support.util.visible
import kotlinx.android.synthetic.main.item_ppob_payment_detail.view.*


/**
 * Created by macari on 3/7/18.
 */

class PpobPaymentDetailAdapter(// Provide a suitable constructor (depends on the kind of dataset)
        private val mContext: Context,
        var mDataset: List<WidgetBuilderModel>) : RecyclerView.Adapter<PpobPaymentDetailAdapter.ViewHolder>() {

    var layout: Int = R.layout.item_ppob_payment_detail

    companion object {
        val VERTICAL = 1
        val HORIZONTAL = 2
    }

    var mOnViewClickListener: OnViewClickListener? = null
    var orientation = HORIZONTAL

    lateinit var displayBarcode: (content: String) -> Unit

    class ViewHolder(val v: View) : RecyclerView.ViewHolder(v)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        if (orientation == VERTICAL) {
            layout = R.layout.item_ppob_payment_detail_vertical
        }

        val view = LayoutInflater.from(parent.context)
                .inflate(layout, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataset[position]

        if (orientation == HORIZONTAL) {
            if (item.key.equals("petunjuk penggunaan", true)) {
                holder.v.tvKeyVertical.text = item.key
                holder.v.tvValueVertical.text = item.value.trim()
                holder.v.findViewById<View>(R.id.horizontalLayout).gone()

                if (item.value.equals("")) {
                    holder.v.verticalLayout.gone()
                } else {
                    holder.v.verticalLayout.visible()
                }
            } else {
                var value = item.value.trim()

                //Check Kode Voucher
                if(item.key.equals("kode voucher", true)){
                    value = AesEncryption.decrypt(value)
                }

                if (item.key.contains("token", true) ||
                        item.key.contains("serial", true) ||
                        item.key.contains("kode", true)) {
                    holder.v.tvCopy.visible()

                    holder.v.tvCopy.setOnClickListener {
                        copyTextToClipBoard(value)
                    }
                }

                holder.v.tvKey.text = item.key
                holder.v.tvValue.text = value
                holder.v.verticalLayout.gone()

                if (item.value == null || item.value.isEmpty()) {
                    holder.v.findViewById<View>(R.id.horizontalLayout).gone()
                } else {
                    holder.v.findViewById<View>(R.id.horizontalLayout).visible()
                }

                var labelNoResi = item.key.replace(".", "")
                        .replace(" ", "")
                if ((labelNoResi.equals("noresi", true)) || (labelNoResi.equals("receiptnumber", true))) {
                    if (::displayBarcode.isInitialized) {
                        displayBarcode(item.value)
                    }
                }
            }
        } else {
            holder.v.tvKey.text = item.key
            holder.v.tvValue.text = item.value.trim()
        }

        holder.v.itemLayout.setOnClickListener {
            mOnViewClickListener?.onViewClickListener(item, position)
        }
    }

    private fun copyTextToClipBoard(value: String?) {
        val clipboard: ClipboardManager? = mContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clip = ClipData.newPlainText("Copied to clipboard", value)
        clipboard?.setPrimaryClip(clip)

        "Berhasil disalin ke clipboard".showToast(mContext)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return mDataset.size
    }

    fun setmOnViewClickListener(mOnViewClickListener: OnViewClickListener) {
        this.mOnViewClickListener = mOnViewClickListener
    }

    interface OnViewClickListener {
        fun onViewClickListener(widgetBuilderModel: WidgetBuilderModel, position: Int)
    }
}