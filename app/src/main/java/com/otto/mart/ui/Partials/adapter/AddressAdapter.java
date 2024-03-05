package com.otto.mart.ui.Partials.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.AddressModel;
import com.otto.mart.ui.activity.register.RecyclerAdapterCallback;

import java.util.ArrayList;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    private List<AddressModel> addresses;
    private RecyclerAdapterCallback callback;

    public AddressAdapter() {
        addresses = new ArrayList<>();
    }

    public AddressAdapter(RecyclerAdapterCallback callback) {
        this.callback = callback;
        addresses = new ArrayList<>();
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        String addressName = addresses.get(position).getName();
        if (position == 0) {
            holder.handleFirstItem();
            addressName = holder.itemView.getContext().getString(R.string.label_main_address);
        } else if (position == 1) {
            holder.handleSecondItem();
        } else {
            holder.addressLabel.setVisibility(View.GONE);
        }

        holder.addressName.setText(addressName);
        holder.address.setText(addresses.get(position).getCompleteAddress());
        String address = addresses.get(position).getDistrictName() + " - " +
                addresses.get(position).getCityName() + " - " +
                addresses.get(position).getProvinceName();
        holder.address1.setText(address);
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    public void setAddresses(List<AddressModel> addresses) {
        this.addresses = addresses;
        notifyDataSetChanged();
    }

    public void addAddress(AddressModel addressModel) {
        int pos = getItemCount() - 1;
        addresses.add(addressModel);
        notifyItemInserted(pos);
    }

    class AddressViewHolder extends RecyclerView.ViewHolder {
        private TextView addressName, address, address1, actionEdit, addressLabel;

        public AddressViewHolder(View itemView) {
            super(itemView);
            addressName = itemView.findViewById(R.id.address_name);
            address = itemView.findViewById(R.id.address);
            address1 = itemView.findViewById(R.id.address1);
            actionEdit = itemView.findViewById(R.id.actionEdit);
            addressLabel = itemView.findViewById(R.id.addressLabel);

            actionEdit.setOnClickListener(v -> {
                try {
                    callback.onItemClick(addresses.get(getAdapterPosition()), getAdapterPosition(), AddressViewHolder.this);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            });
        }

        public void handleFirstItem() {
            addressLabel.setText(addressLabel.getContext().getString(R.string.label_business_address));
            addressLabel.setVisibility(View.VISIBLE);
            actionEdit.setVisibility(View.GONE);
        }

        public void handleSecondItem() {
            addressLabel.setText(addressLabel.getContext().getString(R.string.label_another_address));
            addressLabel.setVisibility(View.VISIBLE);
        }
    }
}
