package com.otto.mart.model.APIModel.Misc.ppob;

import com.otto.mart.model.APIModel.Misc.CategoryModel;

import java.util.List;

public class PpobCategoryPicker {

    private String title = "";
    private List<CategoryModel> categoryModels;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<CategoryModel> getCategoryModels() {
        return categoryModels;
    }

    public void setCategoryModels(List<CategoryModel> categoryModels) {
        this.categoryModels = categoryModels;
    }
}
