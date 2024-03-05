package com.otto.mart.ui.Partials.adapter.ottopoint;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.ottopoint.DealsItemModel;
import com.otto.mart.support.util.DownloadImageManager;
import com.otto.mart.ui.actionView.ActionListItemTwo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.ViewHolder>{

    private Context context;
    private List<DealsItemModel> mItems;
    private ActionListItemTwo callback;

    public DealsAdapter(Context context, List<DealsItemModel> mItels, ActionListItemTwo callback){
        this.context = context;
        this.mItems = mItels;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_deals, viewGroup, false);
        ButterKnife.bind(this, view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        // set view
        viewHolder.tvCompanyName.setText(mItems.get(i).getCompany_name());
        viewHolder.tvTitle.setText(mItems.get(i).getTitle());
        viewHolder.tvPrice.setText(mItems.get(i).getPrice_text());

        // sample
        viewHolder.imgBanner.setImageResource(mItems.get(i).getUrl_img_banner_sample());
        viewHolder.imgCompany.setImageResource(mItems.get(i).getUrl_img_company_logo_sample());

        // download image
        if(!mItems.get(i).getUrl_img_company_logo().isEmpty())
            new DownloadImageManager(context)
                    .setPlaceHolder(R.drawable.img_placeholder_ottopoint_square)
                    .start(mItems.get(i).getUrl_img_company_logo(), viewHolder.imgCompany);

        if(!mItems.get(i).getUrl_img_banner().isEmpty())
            new DownloadImageManager(context)
                    .start(mItems.get(i).getUrl_img_banner(), viewHolder.imgBanner);

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
        viewHolder.btnBeli.setOnClickListener(view -> {
            Bundle sendData = new Bundle();
            sendData.putString("id", mItems.get(i).getId());
            callback.actionItem(i, sendData);
        });

        viewHolder.viewCard.setOnClickListener(view -> {
            Bundle sendData = new Bundle();
            sendData.putString("id", mItems.get(i).getId());
            callback.actionItem(i, sendData);
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.img_banner)
        ImageView imgBanner;
        @BindView(R.id.img_company)
        ImageView imgCompany;
        @BindView(R.id.tv_company_name)
        TextView tvCompanyName;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.btn_beli)
        Button btnBeli;
        @BindView(R.id.view_card)
        View viewCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
