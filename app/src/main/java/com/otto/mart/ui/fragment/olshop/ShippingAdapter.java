package com.otto.mart.ui.fragment.olshop;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Response.olshop.order.ShippingItem;
import com.otto.mart.ui.Partials.RecyclerViewListener;

import java.util.List;

public class ShippingAdapter extends RecyclerView.Adapter<ShippingAdapter.ShippingViewHolder> {

    private List<ShippingItem> itemList;
    private RecyclerViewListener listener;

    public ShippingAdapter(List<ShippingItem> itemList, RecyclerViewListener listener) {
        this.itemList = itemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ShippingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShippingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shipping, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShippingViewHolder holder, int position) {
        holder.bind(itemList.get(position));
        holder.itemView.setOnClickListener(v -> {
            if (listener != null)
                listener.onItemClick(0, position, itemList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ShippingViewHolder extends RecyclerView.ViewHolder {

        private TextView expeditionName, price;

        ShippingViewHolder(View itemView) {
            super(itemView);
            expeditionName = itemView.findViewById(R.id.expeditionName);
            price = itemView.findViewById(R.id.price);
        }

        void bind(ShippingItem shippingItem) {
            expeditionName.setText(shippingItem.getCourier_name());
            price.setText(shippingItem.getShipping_cost());
        }
    }
}
