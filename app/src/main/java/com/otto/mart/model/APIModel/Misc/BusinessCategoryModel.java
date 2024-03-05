package com.otto.mart.model.APIModel.Misc;

import java.util.List;

public class BusinessCategoryModel {
    int id;
    String code;
    String name;
    Boolean isSelected = false;
    List<BusinessCategoryModel> business_categories;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BusinessCategoryModel> getBusiness_categories() {
        return business_categories;
    }

    public void setBusiness_categories(List<BusinessCategoryModel> business_categories) {
        this.business_categories = business_categories;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
