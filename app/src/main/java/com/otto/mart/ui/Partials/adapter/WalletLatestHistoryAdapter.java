package com.otto.mart.ui.Partials.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.history.OmzetHistory;
import com.otto.mart.support.util.DataUtil;

import java.util.List;

/**
 * Created by macari on 3/7/18.
 */

public class WalletLatestHistoryAdapter extends RecyclerView.Adapter<WalletLatestHistoryAdapter.ViewHolder> {

    private Context mContext;
    public List<OmzetHistory> mDataset;

    public OnViewClickListener mOnViewClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout itemLayout;
        public TextView tvName;
        public TextView tvDesc;
        public TextView tvDate;
        public TextView tvNoResi;
        public TextView tvAmount;

        public ViewHolder(View v) {
            super(v);
            itemLayout = v.findViewById(R.id.item_layout);
            tvName = v.findViewById(R.id.tv_name);
            tvDesc = v.findViewById(R.id.tv_desc);
            tvDate = v.findViewById(R.id.tv_date);
            tvNoResi = v.findViewById(R.id.tvNoResi);
            tvAmount = v.findViewById(R.id.tv_amount);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public WalletLatestHistoryAdapter(Context context, List<OmzetHistory> myDataset) {
        mContext = context;
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)x
    @Override
    public WalletLatestHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wallet_latest_history, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        OmzetHistory item = mDataset.get(position);

        holder.tvDate.setText(item.getDate_string());
        holder.tvName.setText(item.getBiller_reference());
        holder.tvDesc.setText(item.getDescription());
        holder.tvNoResi.setText(item.getReference_number());
        holder.tvAmount.setText(DataUtil.convertCurrency(item.getAmount()));

        holder.itemLayout.setOnClickListener(v -> {
           // mOnViewClickListener.onViewClickListener(item, position);
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setmOnViewClickListener(OnViewClickListener mOnViewClickListener) {
        this.mOnViewClickListener = mOnViewClickListener;
    }

    public interface OnViewClickListener {
        void onViewClickListener(OmzetHistory omzetHistory, int position);
    }
}