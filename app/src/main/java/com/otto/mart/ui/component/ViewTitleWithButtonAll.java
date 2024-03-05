package com.otto.mart.ui.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otto.mart.R;

import javax.annotation.Nullable;

public class ViewTitleWithButtonAll extends LinearLayout {

    private View viewAhowAll;

    public ViewTitleWithButtonAll(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    private void init(AttributeSet attrs){
        if(attrs == null) return;

        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_title_with_button_show_all, this);

        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.ViewTitleWithButtonAll);

        TextView tvTitle = view.findViewById(R.id.tv_title);
        viewAhowAll = view.findViewById(R.id.view_show_all);

        int resourceTitle = attributes.getResourceId(R.styleable.ViewTitleWithButtonAll_vtba_title, -1);
        if(resourceTitle != -1)
            tvTitle.setText(resourceTitle);

        TextView tvTextButtonAll = viewAhowAll.findViewById(R.id.tv_text);
        tvTextButtonAll.setText(attributes.getResourceId(R.styleable.ViewTitleWithButtonAll_vtba_title_button, R.string.label_lihat_semua));

        attributes.recycle();
    }

    public void setActionViewAll(OnClickListener callback){
        if(viewAhowAll == null) return;

        viewAhowAll.setOnClickListener(callback);
    }
}
