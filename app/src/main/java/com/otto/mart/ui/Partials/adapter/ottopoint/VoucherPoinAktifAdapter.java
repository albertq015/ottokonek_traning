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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otto.mart.GLOBAL;
import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.ottopoint.VoucherPointItemModel;
import com.otto.mart.support.util.DateUtil;
import com.otto.mart.support.util.DownloadImageManager;
import com.otto.mart.ui.actionView.ActionListItemTwo;
import com.otto.mart.ui.activity.ottopoint.DetailVoucherActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class VoucherPoinAktifAdapter extends RecyclerView.Adapter<VoucherPoinAktifAdapter.ViewHolder>{

    private final int VIEW_DEFAULT = 1;
    private final int VIEW_MULTI = 2;

    private Context context;
    private List<VoucherPointItemModel> mItems;
    private ActionListItemTwo callback;
    private boolean showChild = true;

    public VoucherPoinAktifAdapter(Context context, List<VoucherPointItemModel> mItems, ActionListItemTwo callback){
        this.context = context;
        this.mItems = mItems;
        this.callback = callback;
    }

    public VoucherPoinAktifAdapter(Context context, List<VoucherPointItemModel> mItems, boolean showChild, ActionListItemTwo callback){
        this.context = context;
        this.mItems = mItems;
        this.callback = callback;
        this.showChild = showChild;
    }

    @Override
    public int getItemViewType(int position) {
        return showChild ? mItems.get(position).getJumlah() > 0 ? VIEW_MULTI : VIEW_DEFAULT : VIEW_DEFAULT;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        int resourceLayout = viewType == VIEW_MULTI ? R.layout.list_item_voucher_poin_aktif_two : R.layout.list_item_voucher_poin_aktif;
        View view = LayoutInflater.from(context).inflate(resourceLayout, viewGroup, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        // set view
        viewHolder.tvCompanyName.setText(mItems.get(i).getCompanyName());
        viewHolder.tvTitle.setText(mItems.get(i).getTitle());

        if(getItemViewType(i) == VIEW_MULTI){
            viewHolder.viewJumlahVoucher.setVisibility(View.VISIBLE);
            viewHolder.tvJumlahVoucher.setText((mItems.get(i).getJumlah() + 1) + " " + context.getString(R.string.label_voucher));
            viewHolder.tvExpireDate.setText(mItems.get(i).getExpireDate());
        }else{
            viewHolder.viewJumlahVoucher.setVisibility(View.GONE);

            if(mItems.get(i).getExpireDate() != null && !mItems.get(i).getExpireDate().isEmpty())
                viewHolder.tvExpireDate.setText(
                    DateUtil.getDateAsStringFormat(mItems.get(i).getExpireDate(), GLOBAL.FORMAT_DATE_TIME_DATE_MONTH_TEXT_MIN, GLOBAL.FORMAT_DATE_TIME_DATE_MONTH_TEXT)
                );
        }

        // download image
        if(mItems.get(i).getUrlCompanyPic() != null && !mItems.get(i).getUrlCompanyPic().isEmpty())
            new DownloadImageManager(context)
                    .setPlaceHolder(R.drawable.img_placeholder_ottopoint_square)
                    .start(mItems.get(i).getUrlCompanyPic(), viewHolder.imgCompany);

        if(mItems.get(i).getUrlBanner() != null && !mItems.get(i).getUrlBanner().isEmpty()){
            if(getItemViewType(i) == VIEW_MULTI)
                new DownloadImageManager(context)
                        .startRound(mItems.get(i).getUrlBanner(), viewHolder.imgBanner, 8, RoundedCornersTransformation.CornerType.TOP);
            else
                new DownloadImageManager(context)
                        .start(mItems.get(i).getUrlBanner(), viewHolder.imgBanner);
        }

        if(i == (getItemCount() - 1)){
            int top = (int) context.getResources().getDimension(R.dimen.space_card_top);
            int bottom = (int) context.getResources().getDimension(this.showChild ? R.dimen.space_card_bottom_extra : R.dimen.space_card_bottom);
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

        viewHolder.btnTukar.setOnClickListener(view -> {
            Bundle sendData = new Bundle();
            sendData.putSerializable(DetailVoucherActivity.KEY_DATA, mItems.get(i));

            callback.actionItem(i, sendData);
        });

        viewHolder.viewCard.setOnClickListener(view -> {
            Bundle sendData = new Bundle();
            sendData.putSerializable(DetailVoucherActivity.KEY_DATA, mItems.get(i));

            callback.actionItem(i, sendData);
        });
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.img_banner)
        ImageView imgBanner;
        @BindView(R.id.view_jumlah_voucher)
        View viewJumlahVoucher;
        @BindView(R.id.tv_jumlah_voucher)
        TextView tvJumlahVoucher;
        @BindView(R.id.img_company)
        ImageView imgCompany;
        @BindView(R.id.tv_company_name)
        TextView tvCompanyName;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_expire_date)
        TextView tvExpireDate;
        @BindView(R.id.btn_tukar)
        Button btnTukar;
        @BindView(R.id.view_card)
        View viewCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
