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

public class ActionbarOttopointTransparentWhite extends LinearLayout {

    private ImageView viewBack;
    private TextView tvTitlePage;
    private ImageView viewShare;

    public ActionbarOttopointTransparentWhite(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    private void init(AttributeSet attrs){
        if(attrs == null) return;

        View view = LayoutInflater.from(getContext()).inflate(R.layout.toolbar_ottomart_transparent_white_text, this);

        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.ActionbarOttopointTransparentWhite);

        tvTitlePage = view.findViewById(R.id.tv_title_page);
        viewBack = view.findViewById(R.id.view_back);
        viewShare = view.findViewById(R.id.view_share);

        int resourceTitle = attributes.getResourceId(R.styleable.ActionbarOttopointTransparentWhite_aotw_title, -1);
        boolean resourceIsShowTitle = attributes.getBoolean(R.styleable.ActionbarOttopointTransparentWhite_aotw_is_show_title, true);
        boolean resourceIsShowMenuLeft = attributes.getBoolean(R.styleable.ActionbarOttopointTransparentWhite_aotw_is_show_menu_left, true);
        boolean resourceIsShowMenuRight = attributes.getBoolean(R.styleable.ActionbarOttopointTransparentWhite_aotw_is_show_menu_right, false);

        tvTitlePage.setVisibility(resourceIsShowTitle ? View.VISIBLE : View.INVISIBLE);
        viewBack.setVisibility(resourceIsShowMenuLeft ? View.VISIBLE : View.INVISIBLE);
        viewShare.setVisibility(resourceIsShowMenuRight ? View.VISIBLE : View.INVISIBLE);

        if(resourceTitle != -1)
            tvTitlePage.setText(resourceTitle);

        attributes.recycle();
    }

    public void actionMenuLeft(OnClickListener callback){
        if(viewBack == null) return;

        viewBack.setOnClickListener(callback);
    }

    public void actionMenuRight(OnClickListener callback){
        if(viewShare == null) return;

        viewShare.setOnClickListener(callback);
    }

    public void actionTitle(OnClickListener callback){
        if(tvTitlePage == null) return;

        tvTitlePage.setOnClickListener(callback);
    }
}
