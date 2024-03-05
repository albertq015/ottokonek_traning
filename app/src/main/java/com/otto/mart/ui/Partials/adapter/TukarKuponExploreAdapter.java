package com.otto.mart.ui.Partials.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.TukarPointItem;
import com.otto.mart.support.util.CommonHelper;
import com.otto.mart.ui.actionView.ActionListItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TukarKuponExploreAdapter extends RecyclerView.Adapter<TukarKuponExploreAdapter.ViewHolder>{

    public final static String KEY_ID = "key_id";
    public final static String KEY_SALDO_OTTOPAY = "key_saldo_ottopay";
    public final static String KEY_HARGA = "key_harga";

    private Context context;
    private List<TukarPointItem> mItems;
    private ActionListItem callback;

    public TukarKuponExploreAdapter(Context context, List<TukarPointItem> mItems, ActionListItem callback){
        this.context = context;
        this.mItems = mItems;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_tukar_kupon_explore, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // set view
        String saldoOttopay = context.getString(R.string.label_currency) + " " + CommonHelper.currencyFormat(mItems.get(position).getSaldo_ottopay());
        holder.tvSaldoOttopay.setText(saldoOttopay);
        String harga = CommonHelper.currencyFormat(mItems.get(position).getHarga()) + " " + context.getString(R.string.label_poin);
        holder.tvHarga.setText(harga);

        // events
        holder.btnTukar.setOnClickListener(view -> {
            Bundle sendData = new Bundle();
            sendData.putInt(KEY_ID, mItems.get(position).getId());
            sendData.putString(KEY_SALDO_OTTOPAY, saldoOttopay);
            sendData.putString(KEY_HARGA, harga);

            callback.actionItem(sendData);
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_saldo_ottopay)
        TextView tvSaldoOttopay;
        @BindView(R.id.tv_harga)
        TextView tvHarga;
        @BindView(R.id.btn_tukar)
        Button btnTukar;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
