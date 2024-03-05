package com.otto.mart.ui.Partials.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Response.storeLocation.RegionalResponse;
import com.otto.mart.model.localmodel.ui.ottopoint.VoucherPointItemModel;
import com.otto.mart.ui.actionView.ActionListItemTwo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ListBankAdapter extends RecyclerView.Adapter<ListBankAdapter.ViewHolder> {

    private Context context;
    private List<RegionalResponse> mItems;
    private ActionListItemTwo callback;
    private boolean showChild = true;

    public ListBankAdapter(Context context, List<RegionalResponse> mItems, ActionListItemTwo callback){
        this.context = context;
        this.mItems = mItems;
        this.callback = callback;
    }

    public ListBankAdapter(Context context, List<RegionalResponse> mItems, boolean showChild, ActionListItemTwo callback){
        this.context = context;
        this.mItems = mItems;
        this.callback = callback;
        this.showChild = showChild;
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
