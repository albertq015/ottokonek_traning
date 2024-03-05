package com.otto.mart.model.localmodel.ui;

import android.content.Intent;
import androidx.annotation.Nullable;
import android.view.View;


public class ProductIconModel extends com.otto.mart.model.localmodel.ui.DashboardIconModel {
    private String productPrice;
    private String productDiscountPrice;
    private boolean isSelected;
    private View.OnClickListener listener;
    private Object storedModel;
    private String iconUri;
    private int iconDrawable;
    private boolean disabled;



    public ProductIconModel(String title, Intent target, int iconID, @Nullable String extradataString, @Nullable int extradataInt, String productPrice, @Nullable String productDiscountPrice, boolean disabled) {
        super(title, target, iconID, extradataString, extradataInt);
        this.productPrice = productPrice;
        this.productDiscountPrice = productDiscountPrice;
        this.disabled = disabled;
    }

    public ProductIconModel(String title, Intent target, int iconID, @Nullable String extradataString, @Nullable int extradataInt, String productPrice, String productDiscountPrice, View.OnClickListener listener, boolean disabled) {
        super(title, target, iconID, extradataString, extradataInt);
        this.productPrice = productPrice;
        this.productDiscountPrice = productDiscountPrice;
        this.listener = listener;
        this.disabled = disabled;
    }

    public ProductIconModel(String title, Intent target, int iconID, @Nullable String extradataString, @Nullable int extradataInt, String productPrice, String productDiscountPrice, View.OnClickListener listener, Object storedModel, boolean disabled) {
        super(title, target, iconID, extradataString, extradataInt);
        this.productPrice = productPrice;
        this.productDiscountPrice = productDiscountPrice;
        this.listener = listener;
        this.storedModel = storedModel;
        this.disabled = disabled;
    }

    public ProductIconModel(String title, Intent target, int iconID, @Nullable String extradataString, @Nullable int extradataInt, String productPrice, String productDiscountPrice, View.OnClickListener listener, String iconUri, Object storedModel, boolean disabled) {
        super(title, target, iconID, extradataString, extradataInt);
        this.productPrice = productPrice;
        this.productDiscountPrice = productDiscountPrice;
        this.listener = listener;
        this.storedModel = storedModel;
        this.iconUri = iconUri;
        this.disabled = disabled;
    }

    public ProductIconModel(String title, Intent target, int iconID, @Nullable String extradataString, @Nullable int extradataInt, String productPrice, String productDiscountPrice, View.OnClickListener listener, String iconUri, Object storedModel, int iconDrawable, boolean disabled) {
        super(title, target, iconID, extradataString, extradataInt);
        this.productPrice = productPrice;
        this.productDiscountPrice = productDiscountPrice;
        this.listener = listener;
        this.storedModel = storedModel;
        this.iconUri = iconUri;
        this.iconDrawable = iconDrawable;
        this.disabled = disabled;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDiscountPrice() {
        return productDiscountPrice;
    }

    public void setProductDiscountPrice(String productDiscountPrice) {
        this.productDiscountPrice = productDiscountPrice;
    }

    public View.OnClickListener getListener() {
        return listener;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Object getStoredModel() {
        return storedModel;
    }

    public void setStoredModel(Object storedModel) {
        this.storedModel = storedModel;
    }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    public int getIconDrawable() {
        return iconDrawable;
    }

    public void setIconDrawable(int iconDrawable) {
        this.iconDrawable = iconDrawable;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
