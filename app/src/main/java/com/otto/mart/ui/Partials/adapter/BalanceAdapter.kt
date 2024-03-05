package com.otto.mart.ui.Partials.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.otto.mart.R
import com.otto.mart.model.APIModel.Response.balance.BalancesItem
import com.otto.mart.support.util.DataUtil
import kotlinx.android.synthetic.main.item_balance.view.*

class BalanceAdapter(val listener: ((BalancesItem) -> Unit)?) : androidx.recyclerview.widget.RecyclerView.Adapter<BalanceAdapter.VH>() {

    var balances: MutableList<BalancesItem?>? = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): VH =
            VH(LayoutInflater.from(parent.context).inflate(R.layout.item_balance, parent, false))

    override fun getItemCount(): Int = balances?.size ?: 0

    override fun onBindViewHolder(vh: VH, position: Int) {
        balances?.let { it[position]?.let { item -> vh.bind(item) } }
    }

    inner class VH(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bind(balancesItem: BalancesItem) {
            itemView.tvBalanceName.text = balancesItem.name
            itemView.tvBalanceValue.text =
                    if (balancesItem.formattedBalance.equals("-")) "Rp -"
                    else DataUtil.convertCurrency(balancesItem.balance)

            if (!balancesItem.formattedBalance.equals("-")) {
                balancesItem.name?.let { name ->
                    if (name.contains("Point")) {
                        itemView.tvBalanceValue.text = DataUtil.convertCurrency(balancesItem.balance).replace(itemView.tvBalanceValue.context.getString(R.string.text_currency), "", true) + " " + itemView.tvBalanceValue.context.getString(R.string.text_short_point)
                    } else {
                        itemView.tvBalanceValue.text = DataUtil.convertCurrency(balancesItem.balance)
                    }
                }
            } else itemView.tvBalanceValue.text = balancesItem.formattedBalance

            when (balancesItem.name?.toLowerCase()) {
                "ottocash" -> itemView.ivBalanceLogo.setImageResource(R.drawable.logo_otto_cash)
                "ottopoint" -> itemView.ivBalanceLogo.setImageResource(R.drawable.vector_logo_ottopoint)
                else -> itemView.ivBalanceLogo.setImageResource(R.drawable.vector_logo_ottopay)
            }

            itemView.setOnClickListener { listener?.invoke(balancesItem) }
        }
    }
}