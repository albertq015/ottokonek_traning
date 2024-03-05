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
import com.otto.mart.model.APIModel.Misc.history.OmzetHistory;
import com.otto.mart.support.util.DataUtil;
import com.otto.mart.ui.activity.register.RecyclerAdapterCallback;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.TransactionHistoryViewHolder> {

    private List<OmzetHistory> omsetHistory;
    private RecyclerAdapterCallback callback;

    public OnViewClickListener viewClickListener;

    public TransactionHistoryAdapter(RecyclerAdapterCallback callback) {
        this.callback = callback;
        omsetHistory = new ArrayList<>();
    }

    @NonNull
    @Override
    public TransactionHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionHistoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trasaction_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHistoryViewHolder holder, int position) {
        holder.btnCheckStatus.setVisibility(View.GONE);

        holder.tvDesc.setTextColor(ContextCompat.getColor(OttoMartApp.getContext(), R.color.text_blue));

        if (omsetHistory.get(position).getDirection().equalsIgnoreCase("in")) {
            holder.tvAmount.setText("+" + DataUtil.convertCurrency(omsetHistory.get(position).getAmount()));
            if (omsetHistory.get(position).getPayment_method().toLowerCase().equals("cash")) {
                holder.tvAmount.setTextColor(ContextCompat.getColor(OttoMartApp.getContext(), R.color.brown_grey_two));
            } else {
                holder.tvAmount.setTextColor(ContextCompat.getColor(OttoMartApp.getContext(), R.color.apple_green));
            }
        } else {
            holder.tvAmount.setText("-" + DataUtil.convertCurrency(omsetHistory.get(position).getAmount()));
            holder.tvAmount.setTextColor(ContextCompat.getColor(OttoMartApp.getContext(), R.color.cherry_red));
        }

        //holder.number.setText(omsetHistory.get(position).getReferenceNumber());
        holder.tvDate.setText(DataUtil.getDateString(omsetHistory.get(position).getDate(), DataUtil.DEFAULT_FORMAT, true, 0));

        if (omsetHistory.get(position).getDescription().toLowerCase().contains("transfer ke dompet")) {
            omsetHistory.get(position).setDescription("Transfer ke Bank");
        }
        holder.tvDesc.setText(omsetHistory.get(position).getDescription());

        holder.tvStatus.setText(omsetHistory.get(position).getPayment_method());
        if (omsetHistory.get(position).getDescription().equalsIgnoreCase("Transfer ke Bank")) {
            holder.tvStatus.setText("Transfer");
        }

        holder.btnCheckStatus.setOnClickListener(v -> {
            viewClickListener.onButtonStatusClicked(omsetHistory.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return omsetHistory.size();
    }

    public void setOmsetHistory(List<OmzetHistory> omsetHistory) {
        this.omsetHistory = omsetHistory;
        notifyDataSetChanged();
    }

    public void addOmsetHistory(OmzetHistory model) {
        int pos = getItemCount() - 1;
        this.omsetHistory.add(model);
        notifyItemInserted(pos);
    }

    public void addMore(List<OmzetHistory> model) {
        int pos = getItemCount();
        this.omsetHistory.addAll(model);
        notifyItemInserted(pos);
    }

    class TransactionHistoryViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate;
        TextView tvStatus;
        TextView tvDesc;
        TextView tvAmount;
        Button btnCheckStatus;
        ImageView icon;

        public TransactionHistoryViewHolder(final View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvStatus = itemView.findViewById(R.id.type);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            icon = itemView.findViewById(R.id.icon);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnCheckStatus = itemView.findViewById(R.id.btn_check_status);

            itemView.setOnClickListener(v -> {
                try {
                    callback.onItemClick(omsetHistory.get(getAdapterPosition()), getAdapterPosition(), TransactionHistoryViewHolder.this);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void setViewClickListener(OnViewClickListener viewClickListener) {
        this.viewClickListener = viewClickListener;
    }

    public interface OnViewClickListener {
        void onButtonStatusClicked(OmzetHistory omzetHistory);
    }
}
