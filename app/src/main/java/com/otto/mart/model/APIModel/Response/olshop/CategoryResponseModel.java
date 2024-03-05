package com.otto.mart.model.APIModel.Response.olshop;

import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

public class CategoryResponseModel extends BaseResponseModel {

    CategoryResponseData data;

    public CategoryResponseData getData() {
        return data;
    }

    public void setData(CategoryResponseData data) {
        this.data = data;
    }

    public class CategoryResponseData {
        List<Category> categories;

        public List<Category> getCategories() {
            return categories;
        }

        public void setCategories(List<Category> categories) {
            this.categories = categories;
        }
    }
}
