package com.otto.mart.model.localmodel.ppob;

import com.otto.mart.model.APIModel.Misc.CategoryModel;

import java.util.List;

public class PpobCategoryPicker {

    private String title = "";
    private List<CategoryModel> categoryModel;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<CategoryModel> getCategoryModel() {
        return categoryModel;
    }

    public void setCategoryModel(List<CategoryModel> categoryModel) {
        this.categoryModel = categoryModel;
    }
}
