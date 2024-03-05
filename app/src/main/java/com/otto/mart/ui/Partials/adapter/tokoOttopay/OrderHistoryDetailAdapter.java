package com.otto.mart.ui.Partials.adapter.tokoOttopay;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Response.tokoOttopay.OrderHistoryDetailReesponse;
import com.otto.mart.support.util.DataUtil;

import java.util.List;

/**
 * Created by macari on 3/7/18.
 */

public class OrderHistoryDetailAdapter extends RecyclerView.Adapter<OrderHistoryDetailAdapter.ViewHolder> {

    private Context mContext;
    public List<OrderHistoryDetailReesponse.Data.Order.Items> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvName;
        public TextView tvItemPrice;
        public TextView tvTotalPrice;

        public ViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tv_name);
            tvItemPrice = v.findViewById(R.id.tv_item_price);
            tvTotalPrice = v.findViewById(R.id.tv_total_price);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public OrderHistoryDetailAdapter(Context context, List<OrderHistoryDetailReesponse.Data.Order.Items> myDataset) {
        mContext = context;
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public OrderHistoryDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_history_detail, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        OrderHistoryDetailReesponse.Data.Order.Items item = mDataset.get(position);

        holder.tvName.setText(item.getName() + "");
        holder.tvItemPrice.setText(item.getQuantity() + " x " + DataUtil.convertCurrency(DataUtil.getInt(item.getItem_price())));
        holder.tvTotalPrice.setText(DataUtil.convertCurrency(item.getTotal_price()));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}