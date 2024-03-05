package com.otto.mart.ui.Partials.adapter.ottopoint;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.ottopoint.VoucherPointItemModel;
import com.otto.mart.support.util.DownloadImageManager;
import com.otto.mart.ui.actionView.ActionListItem;
import com.otto.mart.ui.activity.ottopoint.DetailVoucherActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VoucherPoinRiwayatAdapter extends RecyclerView.Adapter<VoucherPoinRiwayatAdapter.ViewHolder> {

    private Context context;
    private List<VoucherPointItemModel> mItems;
    private ActionListItem callback;

    public VoucherPoinRiwayatAdapter(Context context, List<VoucherPointItemModel> mItems, ActionListItem callback){
        this.context = context;
        this.mItems = mItems;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_voucher_poin_riwayat, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        // set view
        viewHolder.tvCompanyName.setText(mItems.get(i).getCompanyName());
        viewHolder.tvTitle.setText(mItems.get(i).getTitle());
        viewHolder.tvUsedDate.setText(mItems.get(i).getUsedDate());

        // download image
        if(!mItems.get(i).getUrlCompanyPic().isEmpty())
            new DownloadImageManager(context)
                    .setPlaceHolder(R.drawable.img_placeholder_ottopoint_square)
                    .start(mItems.get(i).getUrlCompanyPic(), viewHolder.imgCompany);

        if(!mItems.get(i).getUrlBanner().isEmpty())
            new DownloadImageManager(context)
                    .start(mItems.get(i).getUrlBanner(), viewHolder.imgBanner);

        if(i == (getItemCount() - 1)){
            int top = (int) context.getResources().getDimension(R.dimen.space_card_top);
            int bottom = (int) context.getResources().getDimension(R.dimen.space_card_bottom_extra);
            int left = (int) context.getResources().getDimension(R.dimen.space_card_left);
            int right = (int) context.getResources().getDimension(R.dimen.space_card_right);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(left, top, right, bottom);
            viewHolder.viewCard.setLayoutParams(params);
        }else{
            int top = (int) context.getResources().getDimension(R.dimen.space_card_top);
            int bottom = (int) context.getResources().getDimension(R.dimen.space_card_bottom);
            int left = (int) context.getResources().getDimension(R.dimen.space_card_left);
            int right = (int) context.getResources().getDimension(R.dimen.space_card_right);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(left, top, right, bottom);
            viewHolder.viewCard.setLayoutParams(params);
        }

        // events
        viewHolder.viewCard.setOnClickListener(v -> {
            Bundle sendData = new Bundle();
            sendData.putSerializable(DetailVoucherActivity.KEY_DATA, mItems.get(i));

            callback.actionItem(sendData);
        });

        viewHolder.viewCard.setOnClickListener(view -> {
            Bundle sendData = new Bundle();
            sendData.putSerializable(DetailVoucherActivity.KEY_DATA, mItems.get(i));

            callback.actionItem(sendData);
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.img_banner)
        ImageView imgBanner;
        @BindView(R.id.img_company)
        ImageView imgCompany;
        @BindView(R.id.tv_company_name)
        TextView tvCompanyName;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.view_jumlah_voucher)
        View viewJumlahVoucher;
        @BindView(R.id.tv_jumlah_voucher)
        TextView tvJumlahVoucher;
        @BindView(R.id.tv_used_date)
        TextView tvUsedDate;
        @BindView(R.id.view_main)
        View viewCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
