package com.otto.mart.ui.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otto.mart.R;

import javax.annotation.Nullable;

public class ActionbarOttopointWhite extends LinearLayout {

    private ImageView viewBack;
    private View viewPoinText;
    private TextView tvHeaderPoinOttopoint;
    private View viewVoucherSayaDetail;
    private View viewRiwayatTukarKupon;
    private View viewDeals;

    public ActionbarOttopointWhite(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    private void init(AttributeSet attrs){
        if(attrs == null) return;

        View view = LayoutInflater.from(getContext()).inflate(R.layout.toolbar_white, this);

        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.ActionbarOttopointWhite);

        viewBack = view.findViewById(R.id.view_back);
        viewPoinText = view.findViewById(R.id.view_poin_text);
        tvHeaderPoinOttopoint = view.findViewById(R.id.tv_header_poin_ottopoint);
        TextView tvTitlePage = view.findViewById(R.id.tv_title_page);
        viewVoucherSayaDetail = view.findViewById(R.id.view_voucher_saya_detail);
        viewRiwayatTukarKupon = view.findViewById(R.id.view_riwayat_tukar_kupon);
        viewDeals = view.findViewById(R.id.view_deals);

        int resourceTitle = attributes.getResourceId(R.styleable.ActionbarOttopointWhite_aow_title, R.string.empty_string);
        boolean resourceIsShowPoint = attributes.getBoolean(R.styleable.ActionbarOttopointWhite_aow_is_show_point, false);
        boolean resourceIsShowDeals = attributes.getBoolean(R.styleable.ActionbarOttopointWhite_aow_is_show_deals, false);
        boolean resourceIsShowTitle = attributes.getBoolean(R.styleable.ActionbarOttopointWhite_aow_is_show_title, true);
        boolean resourceIsShowTukarKupon = attributes.getBoolean(R.styleable.ActionbarOttopointWhite_aow_is_show_tukar_kupon, false);
        boolean resourceIsShowVoucherSaya = attributes.getBoolean(R.styleable.ActionbarOttopointWhite_aow_is_show_voucher_saya, false);

        viewPoinText.setVisibility(resourceIsShowPoint ? View.VISIBLE : View.GONE);
        viewVoucherSayaDetail.setVisibility(resourceIsShowVoucherSaya ? View.VISIBLE : View.GONE);
        viewRiwayatTukarKupon.setVisibility(resourceIsShowTukarKupon ? View.VISIBLE : View.GONE);
        viewDeals.setVisibility(resourceIsShowDeals ? View.VISIBLE : View.GONE);
        tvTitlePage.setVisibility(resourceIsShowTitle ? View.VISIBLE : View.GONE);
        tvTitlePage.setText(resourceTitle);

        attributes.recycle();
    }

    public void setActionMenuLeft(OnClickListener callback){
        if(viewBack == null) return;

        viewBack.setOnClickListener(callback);
    }

    public void setActionPointText(OnClickListener callback){
        if(viewPoinText == null) return;

        viewPoinText.setOnClickListener(callback);
    }

    public void setActionVoucherSayaDetail(OnClickListener callback){
        if(viewVoucherSayaDetail == null) return;

        viewVoucherSayaDetail.setOnClickListener(callback);
    }

    public void setActionTukarKupon(OnClickListener callback){
        if(viewRiwayatTukarKupon == null) return;

        viewRiwayatTukarKupon.setOnClickListener(callback);
    }

    public void setActionDeals(OnClickListener callback){
        if(viewDeals == null) return;

        viewDeals.setOnClickListener(callback);
    }

    public void setTextPoint(String textPoint){
        if(tvHeaderPoinOttopoint == null) return;

        tvHeaderPoinOttopoint.setText(textPoint);
    }
}
