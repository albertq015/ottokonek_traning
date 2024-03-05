package com.otto.mart.ui.Partials.adapter;

import android.app.Activity;
import android.content.Intent;
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
import com.otto.mart.ui.Partials.IntentButtonViewGroup;
import com.otto.mart.ui.activity.Topup.TopupActivity;
import com.otto.mart.ui.activity.qr.scan.ScanQrActivity;
import com.otto.mart.ui.activity.transaction.history.HistoryActivity;
import com.otto.mart.ui.activity.deposit.TransferSaldoActivity;

import java.util.ArrayList;
import java.util.List;

public class CardWalletAdapter extends RecyclerView.Adapter<CardWalletAdapter.WalletViewHolder> {
    private Activity activity;
    private List<WalletDataModel> walletCardviewModels;

    public CardWalletAdapter(Activity activity) {
        this.activity = activity;
        walletCardviewModels = new ArrayList<>();
    }

    @NonNull
    @Override
    public WalletViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WalletViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dompet, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WalletViewHolder holder, int position) {
        if (position == 0) {
            holder.setAsMain();
        } else if (position == 1) {
            holder.setAsSecondary();
        }

        holder.name.setText(walletCardviewModels.get(position).getOwner_name());
        holder.phone.setText(walletCardviewModels.get(position).getAccount_number());
        holder.balance.setText(DataUtil.convertCurrency(DataUtil.convertLongFromCurrency(
                walletCardviewModels.get(position).getBalance())));

        holder.walletName.setText(walletCardviewModels.get(position).getWallet_type_name());

        List<com.otto.mart.model.localmodel.ui.DashboardIconModel> shopcontent = new ArrayList<>();

        Intent wallet = new Intent(activity, HistoryActivity.class);
        wallet.putExtra("walletId", walletCardviewModels.get(position).getId());

        shopcontent.add(new com.otto.mart.model.localmodel.ui.DashboardIconModel("Top Up", new Intent(activity, TopupActivity.class), R.drawable.icon_dompet_topup, null, 0));
        shopcontent.add(new com.otto.mart.model.localmodel.ui.DashboardIconModel("Bayar ke Bank", new Intent(activity, TransferSaldoActivity.class), R.drawable.icon_dompet_kirim_uang, null, 0));
        //shopcontent.add(new com.otto.mart.model.localmodel.ui.DashboardIconModel("Tarik Tunai", new Intent(activity, NewFeatureActivity.class), R.drawable.icon_button_tariktunai, null, 2));
        shopcontent.add(new com.otto.mart.model.localmodel.ui.DashboardIconModel("Belanja\nPakai QR", new Intent(activity, ScanQrActivity.class), R.drawable.icon_button_belanjaqr, null, 3));
        shopcontent.add(new com.otto.mart.model.localmodel.ui.DashboardIconModel("Riwayat", wallet, R.drawable.icon_button_riwayattransaksi, null, 4));
        holder.nav.initWidget(activity, shopcontent, 4);
    }

    @Override
    public int getItemCount() {
        return walletCardviewModels.size();
    }

    public void setWallets(List<WalletDataModel> walletCardviewModels) {
        this.walletCardviewModels = walletCardviewModels;
        notifyDataSetChanged();
    }

    class WalletViewHolder extends RecyclerView.ViewHolder {

        TextView type, phone, name, balance, walletName;
        ImageView walletImage;
        IntentButtonViewGroup nav;

        public WalletViewHolder(View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.type);
            phone = itemView.findViewById(R.id.phone);
            name = itemView.findViewById(R.id.name);
            balance = itemView.findViewById(R.id.balance);
            walletImage = itemView.findViewById(R.id.walletImage);
            nav = itemView.findViewById(R.id.nav);
            walletName = itemView.findViewById(R.id.walletName);

            type.setVisibility(View.GONE);
        }

        private void setAsMain() {
            type.setText(type.getContext().getString(R.string.text_wallet_main_transaction));
            type.setVisibility(View.VISIBLE);
        }


        public void setAsSecondary() {
            type.setText(activity.getString(R.string.text_wallet_another_transaction));
            type.setVisibility(View.VISIBLE);
        }
    }
}
