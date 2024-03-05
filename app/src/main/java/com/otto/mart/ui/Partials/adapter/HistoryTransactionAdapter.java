package com.otto.mart.ui.Partials.adapter;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.otto.mart.OttoMartApp;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.OmzetHistoryResponseData;
import com.otto.mart.model.APIModel.Misc.history.OttoCashHistory;
import com.otto.mart.support.util.DataUtil;
import com.otto.mart.ui.activity.register.RecyclerAdapterCallback;

import java.util.ArrayList;
import java.util.List;

public class HistoryTransactionAdapter extends RecyclerView.Adapter<HistoryTransactionAdapter.TransactionHistoryViewHolder> {

    private List<OttoCashHistory> omsetHistory;
    private RecyclerAdapterCallback callback;

    public OnViewClickListener viewClickListener;

    public HistoryTransactionAdapter(RecyclerAdapterCallback callback) {
        this.callback = callback;
        omsetHistory = new ArrayList<>();
    }

    @NonNull
    @Override
    public TransactionHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionHistoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_transaction, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHistoryViewHolder holder, int position) {
        holder.btnCheckStatus.setVisibility(View.GONE);

        if (omsetHistory.get(position).getTransactionType().equalsIgnoreCase("c")) {
            holder.tvAmount.setTextColor(ContextCompat.getColor(OttoMartApp.getContext(), R.color.apple_green));
            holder.imageIcon.setImageResource(R.drawable.cashin);
            holder.tvAmount.setText("+" + DataUtil.convertCurrency(omsetHistory.get(position).getAmount()));
        } else if (omsetHistory.get(position).getTransactionType().contains("Sedang Diproses")) {
            holder.tvAmount.setTextColor(ContextCompat.getColor(OttoMartApp.getContext(), R.color.text_blue));
            holder.tvAmount.setText(DataUtil.convertCurrency(omsetHistory.get(position).getAmount()));
            //    holder.btnCheckStatus.setVisibility(View.VISIBLE);
        } else if(omsetHistory.get(position).getTransactionType().equalsIgnoreCase("d")){
            holder.tvAmount.setTextColor(ContextCompat.getColor(OttoMartApp.getContext(), R.color.cherry_red));
            holder.imageIcon.setImageResource(R.drawable.cashout);
            holder.tvAmount.setText("-" + DataUtil.convertCurrency(omsetHistory.get(position).getAmount()));
        }  else {
            holder.tvAmount.setTextColor(ContextCompat.getColor(OttoMartApp.getContext(), R.color.text_blue));
            holder.tvAmount.setText("-" + DataUtil.convertCurrency(omsetHistory.get(position).getAmount()));
        }

        holder.tvDate.setText(omsetHistory.get(position).getValueDate());
        holder.tvDesc.setText(omsetHistory.get(position).getDescription());
        holder.tvStatus.setText(omsetHistory.get(position).getStatus());
        holder.tvNoReff.setText(omsetHistory.get(position).getReferenceNumber());

        holder.btnCheckStatus.setOnClickListener(v -> {
            //viewClickListener.onButtonStatusClicked(omsetHistory.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return omsetHistory.size();
    }

    public void setOmsetHistory(List<OttoCashHistory> omsetHistory) {
        this.omsetHistory = omsetHistory;
        notifyDataSetChanged();
    }

    public void addOmsetHistory(OttoCashHistory model) {
        int pos = getItemCount() - 1;
        this.omsetHistory.add(model);
        notifyItemInserted(pos);
    }

    public void addMore(List<OttoCashHistory> model) {
        int pos = getItemCount();
        this.omsetHistory.addAll(model);
        notifyItemInserted(pos);
    }

    class TransactionHistoryViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate;
        TextView tvStatus;
        TextView tvNoReff;
        TextView tvDesc;
        TextView tvAmount;
        Button btnCheckStatus;
        ImageView icon;
        ImageView imageIcon;

        public TransactionHistoryViewHolder(final View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvStatus = itemView.findViewById(R.id.type);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            icon = itemView.findViewById(R.id.icon);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvNoReff = itemView.findViewById(R.id.tvNoReff);
            btnCheckStatus = itemView.findViewById(R.id.btn_check_status);
            imageIcon = itemView.findViewById(R.id.imageIcon);

            /*itemView.setOnClickListener(v -> {
                try {
                    callback.onItemClick(omsetHistory.get(getAdapterPosition()), getAdapterPosition(), TransactionHistoryViewHolder.this);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            });*/
        }
    }

    public void setViewClickListener(OnViewClickListener viewClickListener) {
        this.viewClickListener = viewClickListener;
    }

    public interface OnViewClickListener {
        void onButtonStatusClicked(OmzetHistoryResponseData omzetHistoryResponseData);
    }
}

