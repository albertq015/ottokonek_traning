package com.otto.mart.model.localmodel.ui.ottopoint;

import android.graphics.drawable.Drawable;

public class DealsItem {

    private int id = -1;
    private String title = "";
    private Drawable image;

    public DealsItem(int id, String title, Drawable image){
        this.id = id;
        this.title = title;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}
