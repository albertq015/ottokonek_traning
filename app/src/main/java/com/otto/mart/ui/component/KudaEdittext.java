package com.otto.mart.ui.component;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.otto.mart.R;

public class KudaEdittext extends LazyEdittext {
    private ImageView kudaImgv;
    private Context mContext;

    public KudaEdittext(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initAdditionalComponent();
    }

    private void initAdditionalComponent() {
        kudaImgv = findViewById(R.id.imgv);
    }

    public void addLogo(String Uri) {
        Glide.with(mContext).load(Uri).into(kudaImgv);
    }

    public void addLogo(int resid) {
        Glide.with(mContext).load(resid).into(kudaImgv);
    }

    public void addLogo(String Uri, int width, int height) {
        Glide.with(mContext).load(Uri)
                .apply(new RequestOptions().override(width, height))
                .into(kudaImgv);
    }
}