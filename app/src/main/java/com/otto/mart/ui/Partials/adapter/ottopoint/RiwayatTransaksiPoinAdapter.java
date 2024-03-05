package com.otto.mart.ui.Partials.adapter.ottopoint;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.ottopoint.RiwayatTransaksiPointItemModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RiwayatTransaksiPoinAdapter extends RecyclerView.Adapter<RiwayatTransaksiPoinAdapter.ViewHolder>{

    private Context context;
    private List<RiwayatTransaksiPointItemModel> mItems;

    public RiwayatTransaksiPoinAdapter(Context context, List<RiwayatTransaksiPointItemModel> mItems){
        this.context = context;
        this.mItems = mItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_riwayat_transaksi_ottopoint, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        // set view
        viewHolder.tvDate.setText(mItems.get(i).getDate());
        viewHolder.tvTitle.setText(mItems.get(i).getTitle());
        viewHolder.tvTypeTransaction.setText(mItems.get(i).getType_transaction());
        viewHolder.tvPoint.setText(mItems.get(i).getPoint_text());

        viewHolder.tvPoint.setTextColor(
                mItems.get(i).isPlus() ?
                        context.getResources().getColor(R.color.ocean_blue) : context.getResources().getColor(R.color.red_soft_two)
        );
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_type_transaction)
        TextView tvTypeTransaction;
        @BindView(R.id.tv_point)
        TextView tvPoint;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
