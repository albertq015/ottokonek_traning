package com.otto.mart.model.localmodel.ui.ottopoint;

public class DefaultItemModel {

    private int id = -1;
    private String text = "";
    private boolean selected = false;

    public DefaultItemModel(int id, String text){
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
