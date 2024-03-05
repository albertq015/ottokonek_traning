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

public class TukarKuponSayaAdapter extends RecyclerView.Adapter<TukarKuponSayaAdapter.ViewHolder>{

    public final static String KEY_ID = "key_id";
    public final static String KEY_SALDO_OTTOPAY = "key_saldo_ottopay";
    public final static String KEY_SALDO_OTTOPAY_TEXT = "key_saldo_ottopay_text";
    public final static String KEY_HARGA = "key_harga";

    private Context context;
    private List<TukarPointItem> mItems;
    private ActionListItem callback;

    public TukarKuponSayaAdapter(Context context, List<TukarPointItem> mItems, ActionListItem callback){
        this.context = context;
        this.mItems = mItems;
        this.callback = callback;
    }

    @NonNull
    @Override
    public TukarKuponSayaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_tukar_kupon_saya, parent, false);
        return new TukarKuponSayaAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TukarKuponSayaAdapter.ViewHolder holder, int position) {
        // set view
        String saldoOttopay = context.getString(R.string.label_currency) + " " + CommonHelper.currencyFormat(mItems.get(position).getSaldo_ottopay());
        holder.tvSaldoOttopay.setText(saldoOttopay);

        holder.tvStatus.setText(mItems.get(position).isActive() ? context.getString(R.string.label_aktif) : context.getString(R.string.label_tidak_aktif));

        // events
        holder.btnDetail.setOnClickListener(view -> {
            Bundle sendData = new Bundle();
            sendData.putInt(KEY_ID, mItems.get(position).getId());
            sendData.putString(KEY_SALDO_OTTOPAY, saldoOttopay);
            sendData.putString(KEY_SALDO_OTTOPAY_TEXT, context.getString(R.string.label_saldo) + " " + context.getString(R.string.label_currency) + " " + CommonHelper.currencyFormat(mItems.get(position).getSaldo_ottopay()));

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
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.btn_detail)
        Button btnDetail;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
