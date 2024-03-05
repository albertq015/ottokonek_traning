package com.otto.mart.ui.Partials.adapter.tokoOttopay;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.tokoOttopay.OrderHistory;
import com.otto.mart.support.util.DataUtil;

import java.util.List;

/**
 * Created by macari on 3/7/18.
 */

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    private Context mContext;
    public List<OrderHistory> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvNoOrder;
        public TextView tvTotal;
        public TextView tvOrderStatus;
        public TextView tvPaymentStatus;

        public ViewHolder(View v) {
            super(v);
            tvNoOrder = v.findViewById(R.id.tv_no_order);
            tvTotal = v.findViewById(R.id.tv_total);
            tvOrderStatus = v.findViewById(R.id.tv_order_status);
            tvPaymentStatus = v.findViewById(R.id.tv_payment_status);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public OrderHistoryAdapter(Context context, List<OrderHistory> myDataset) {
        mContext = context;
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public OrderHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_history, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        OrderHistory item = mDataset.get(position);

        double totalAmount = Double.valueOf(item.getTotal_amount());

        holder.tvNoOrder.setText(item.getOrder_number());
        holder.tvTotal.setText(DataUtil.convertCurrency(totalAmount));
        holder.tvOrderStatus.setText(item.getStatus());
        holder.tvPaymentStatus.setText(item.getPayment_status());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}