package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.BusinessCategoryModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BusinessCategoryResponseModel extends BaseResponseModel {
    BusinessCategoryDataModel data;

    public BusinessCategoryDataModel getData() {
        return data;
    }

    public void setData(BusinessCategoryDataModel data) {
        this.data = data;
    }


    public List<BusinessCategoryModel> getBusiness_types() {
        return data.business_types;
    }

    public void setBusiness_types(List<BusinessCategoryModel> business_types) {
        data.business_types = business_types;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class BusinessCategoryDataModel {
    List<BusinessCategoryModel> business_types;

    public List<BusinessCategoryModel> getBusiness_types() {
        return business_types;
    }

    public void setBusiness_types(List<BusinessCategoryModel> business_types) {
        this.business_types = business_types;
    }
}
