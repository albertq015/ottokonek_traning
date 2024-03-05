package com.otto.mart.model.localmodel.ui.ottopoint;

import android.graphics.drawable.Drawable;

public class ViewPagerItem {

    private String id;
    private String urlImage;
    private Drawable image;
    private Object data;

    public ViewPagerItem(String id, String urlImage, Drawable image){
        this.id = id;
        this.urlImage = urlImage;
        this.image = image;
    }

    public ViewPagerItem(String id, String urlImage, Object data){
        this.id = id;
        this.urlImage = urlImage;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
