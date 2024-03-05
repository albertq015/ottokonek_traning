package com.otto.mart.model.localmodel.ui;

public class FilterItemModel {
    private String filterType;
    private int id;
    private boolean isSelected;
    private Object storedObject;

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Object getStoredObject() {
        return storedObject;
    }

    public void setStoredObject(Object storedObject) {
        this.storedObject = storedObject;
    }
}
