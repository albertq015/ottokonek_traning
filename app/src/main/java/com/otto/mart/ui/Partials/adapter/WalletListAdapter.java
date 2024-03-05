package com.otto.mart.ui.Partials.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.WalletDataModel;
import com.otto.mart.support.util.DataUtil;
import com.otto.mart.ui.activity.register.RecyclerAdapterCallback;

import java.util.ArrayList;
import java.util.List;

public class WalletListAdapter extends RecyclerView.Adapter<WalletListAdapter.WalletListViewHolder> {
    private RecyclerAdapterCallback callback;
    private List<WalletDataModel> wallets;

    public WalletListAdapter(RecyclerAdapterCallback callback) {
        this.callback = callback;
        wallets = new ArrayList<>();
    }

    @NonNull
    @Override
    public WalletListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WalletListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wallet, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WalletListViewHolder holder, int position) {
        if (position == 0) {
            holder.viewAsFirst();
        }
        if (position == 1) {
            holder.viewAsSecond();
        }

        holder.number.setText(DataUtil.getXXXPhone(wallets.get(position).getAccount_number()));
        holder.name.setText(wallets.get(position).getOwner_name());
        holder.saldo.setText(DataUtil.convertCurrency(DataUtil.convertLongFromCurrency(wallets.get(position).getBalance())));
    }

    @Override
    public int getItemCount() {
        return wallets.size();
    }

    public void setWallets(List<WalletDataModel> wallets) {
        this.wallets = wallets;
        notifyDataSetChanged();
    }

    public void addWallet(WalletDataModel model) {
        wallets.add(model);
        notifyItemInserted(getItemCount() - 1);
    }

    class WalletListViewHolder extends RecyclerView.ViewHolder {

        private TextView label, number, saldo, name;
        private ImageView img;

        public WalletListViewHolder(View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.label);
            number = itemView.findViewById(R.id.number);
            name = itemView.findViewById(R.id.name);
            saldo = itemView.findViewById(R.id.saldo);
            img = itemView.findViewById(R.id.imgv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        callback.onItemClick(null, getAdapterPosition(), WalletListViewHolder.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        public void viewAsFirst() {
            label.setVisibility(View.VISIBLE);
            label.setText(label.getContext().getString(R.string.text_main_wallet));
        }

        public void viewAsSecond() {
            label.setVisibility(View.VISIBLE);
            label.setText(label.getContext().getString(R.string.text_another_wallet));
        }
    }
}
