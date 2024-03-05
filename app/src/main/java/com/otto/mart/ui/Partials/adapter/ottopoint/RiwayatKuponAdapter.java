package com.otto.mart.ui.Partials.adapter.ottopoint;

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
import com.otto.mart.model.localmodel.ui.ottopoint.RiwayatKuponItem;
import com.otto.mart.support.util.CommonHelper;
import com.otto.mart.ui.actionView.ActionListItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RiwayatKuponAdapter extends RecyclerView.Adapter<RiwayatKuponAdapter.ViewHolder> {

    public static final String KEY_ID = "key_id";
    public static final String KEY_TITLE = "key_title";

    private Context context;
    private List<RiwayatKuponItem> mItems;
    private ActionListItem callback;

    public RiwayatKuponAdapter(Context context, List<RiwayatKuponItem> mItems, ActionListItem callback){
        this.context = context;
        this.mItems = mItems;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_riwayat_kupon, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // set view
        //holder.tvStatus.setText(mItems.get(position).getCompanyCode());

        String operator = mItems.get(position).isPlus() ? "+" : "-";
        holder.tvSaldo.setText(operator + context.getString(R.string.label_currency) + " " + CommonHelper.currencyFormat(mItems.get(position).getHarga()));

        // events

        holder.btnDetail.setOnClickListener(view -> {
            Bundle dataSend = new Bundle();
            dataSend.putInt(KEY_ID, mItems.get(position).getId());
            dataSend.putString(KEY_TITLE, context.getString(R.string.label_saldo) + " " + context.getString(R.string.label_currency) + " " + CommonHelper.currencyFormat(mItems.get(position).getHarga()));
            callback.actionItem(dataSend);
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.tv_saldo)
        TextView tvSaldo;
        @BindView(R.id.btn_detail)
        Button btnDetail;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
