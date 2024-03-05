package com.otto.mart.ui.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.support.util.CommonHelper;

import javax.annotation.Nullable;

public class ViewTotalHargaPoint extends LinearLayout {

    private TextView tvPoint;

    public ViewTotalHargaPoint(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    private void init(AttributeSet attrs){
        if(attrs == null) return;

        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_total_harga_point, this);

        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.ViewTotalHargaPoint);

        tvPoint = view.findViewById(R.id.tv_point);

        int resourceTitle = attributes.getResourceId(R.styleable.ViewTotalHargaPoint_vthp_text_point, -1);
        if(resourceTitle != -1)
            tvPoint.setText(resourceTitle);

        attributes.recycle();
    }

    @SuppressLint("SetTextI18n")
    public void setTextPoint(Long point){
        if(tvPoint == null) return;

        tvPoint.setText(CommonHelper.currencyFormat(point) + " " + getContext().getString(R.string.label_poin));
    }
}
