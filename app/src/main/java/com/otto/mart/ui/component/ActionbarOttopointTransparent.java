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

public class ActionbarOttopointTransparent extends LinearLayout {

    private ImageView viewBack;
    private TextView tvTitlePage;

    public ActionbarOttopointTransparent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    private void init(AttributeSet attrs){
        if(attrs == null) return;

        View view = LayoutInflater.from(getContext()).inflate(R.layout.toolbar_ottomart_transparent, this);

        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.ActionbarOttopointTransparent);

        tvTitlePage = view.findViewById(R.id.tv_title_page);
        viewBack = view.findViewById(R.id.view_back);

        int resourceTitle = attributes.getResourceId(R.styleable.ActionbarOttopointTransparent_aot_title, -1);

        if(resourceTitle != -1)
            tvTitlePage.setText(resourceTitle);

        attributes.recycle();
    }

    public void setActionMenuLeft(OnClickListener callback){
        if(viewBack == null) return;

        viewBack.setOnClickListener(callback);
    }

    public void setActionTitle(OnClickListener callback){
        if(tvTitlePage == null) return;

        tvTitlePage.setOnClickListener(callback);
    }
}
