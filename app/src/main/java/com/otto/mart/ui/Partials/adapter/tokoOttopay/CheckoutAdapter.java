package com.otto.mart.ui.Partials.adapter.tokoOttopay;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.tokoOttopay.Cart;
import com.otto.mart.support.util.DataUtil;

import java.util.List;

/**
 * Created by macari on 3/7/18.
 */

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder> {

    private Context mContext;
    public List<Cart> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvName;
        public TextView tvQty;
        public TextView tvPrice;
        public TextView tvTotalPrice;

        public ViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tv_name);
            tvQty = v.findViewById(R.id.tv_qty);
            tvPrice = v.findViewById(R.id.tv_price);
            tvTotalPrice = v.findViewById(R.id.tv_total_price);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CheckoutAdapter(Context context, List<Cart> myDataset) {
        mContext = context;
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CheckoutAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_checkout, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Cart item = mDataset.get(position);

        int totalPrice = item.getQuantity() * Integer.valueOf(item.getItem_price());

        holder.tvName.setText(item.getProduct_name());
        holder.tvQty.setText(item.getQuantity() + "x");
        holder.tvPrice.setText(DataUtil.convertCurrency(item.getItem_price()));
        holder.tvTotalPrice.setText(DataUtil.convertCurrency(totalPrice));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface OnCartButtonListener {
        void onPlusButtonCLicked(Cart cart, int qty, int position);

        void onMinButtonCLicked(Cart cart, int qty, int position);

        void onQtyChanged(Cart cart, int qty, int position);
    }
}