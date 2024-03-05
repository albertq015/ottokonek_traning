package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.CategoryModel;

import java.util.List;

import app.beelabs.com.codebase.base.response.BaseResponse;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetCategoryItemRequestModel extends BaseResponse {

    public class CategoryRequestData {
        List<CategoryModel> category;

        public List<CategoryModel> getCategory() {
            return category;
        }

        public void setCategory(List<CategoryModel> category) {
            this.category = category;
        }
    }

    CategoryRequestData data;

    public CategoryRequestData getData() {
        return data;
    }

    public void setData(CategoryRequestData data) {
        this.data = data;
    }
}
