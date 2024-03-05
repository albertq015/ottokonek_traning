package com.otto.mart.model.localmodel;

import android.view.View;

public class ActionButtonModel {

    private String textContent;
    private int ImageResourceContent;
    private View.OnClickListener onClickActionContent;
    private Object extraData;

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public int getImageResourceContent() {
        return ImageResourceContent;
    }

    public void setImageResourceContent(int imageResourceContent) {
        ImageResourceContent = imageResourceContent;
    }

    public View.OnClickListener getOnClickActionContent() {
        return onClickActionContent;
    }

    public void setOnClickActionContent(View.OnClickListener onClickActionContent) {
        this.onClickActionContent = onClickActionContent;
    }

    public Object getExtraData() {
        return extraData;
    }

    public void setExtraData(Object extraData) {
        this.extraData = extraData;
    }
}
