package com.otto.mart.ui.Partials.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.WalletType;
import com.otto.mart.ui.Partials.RecyclerViewListener;

import java.util.ArrayList;
import java.util.List;

public class WalletTypeAdapter extends RecyclerView.Adapter<WalletTypeAdapter.WalletTypeViewHolder> {

    private List<WalletType> walletTypeList;
    private RecyclerViewListener listener;

    public WalletTypeAdapter(RecyclerViewListener listener) {
        this.listener = listener;
        walletTypeList = new ArrayList<>();
    }

    @NonNull
    @Override
    public WalletTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WalletTypeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wallet_type, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WalletTypeViewHolder holder, int position) {
        holder.walletName.setText(walletTypeList.get(position).getName());
        Glide.with(holder.walletImage.getContext()).load(walletTypeList.get(position).getLogo_url())
                .into(holder.walletImage);
    }

    @Override
    public int getItemCount() {
        return walletTypeList.size();
    }

    public void setWalletTypeList(List<WalletType> walletTypeList) {
        this.walletTypeList = walletTypeList;
        notifyDataSetChanged();
    }

    class WalletTypeViewHolder extends RecyclerView.ViewHolder {

        private ImageView walletImage;
        private TextView walletName;

        WalletTypeViewHolder(View itemView) {
            super(itemView);
            walletImage = itemView.findViewById(R.id.walletImage);
            walletName = itemView.findViewById(R.id.walletName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        listener.onItemClick(0, getAdapterPosition(), walletTypeList.get(getAdapterPosition()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
