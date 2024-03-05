package com.otto.mart.support.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.otto.mart.R;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class DownloadImageManager {

    private Context context;
    private int imgPlaceHolderResource = R.drawable.vector_img_placeholder_ottopoint;

    public DownloadImageManager(Context context){
        this.context = context;
    }

    public void start(String url, ImageView imageView){
        if(context == null) return;

        configureGlide(imgPlaceHolderResource)
                .load(url)
                .into(imageView);
    }

    public void startRound(String url, ImageView imageView, int radius){
        if(context == null) return;

        configureGlide(imgPlaceHolderResource)
                .load(url)
                .apply(bitmapTransform(new MultiTransformation(
                        new CenterCrop(),
                        new RoundedCornersTransformation(radius, 0, RoundedCornersTransformation.CornerType.ALL))
                ))
                .into(imageView);
    }

    public void startRound(String url, ImageView imageView, int radius, RoundedCornersTransformation.CornerType cornerType){
        if(context == null) return;

        configureGlide(imgPlaceHolderResource)
                .load(url)
                .apply(bitmapTransform(new MultiTransformation(
                        new CenterCrop(),
                        new RoundedCornersTransformation(radius, 0, cornerType))
                ))
                .into(imageView);
    }

    public DownloadImageManager setPlaceHolder(int imgPlaceHolderResource){
        this.imgPlaceHolderResource = imgPlaceHolderResource;

        return this;
    }

    private RequestManager configureGlide(int placeHolder){
        return Glide.with(context).applyDefaultRequestOptions(
                new RequestOptions()
                        .error(placeHolder)
                        .placeholder(placeHolder)
        );
    }
}
