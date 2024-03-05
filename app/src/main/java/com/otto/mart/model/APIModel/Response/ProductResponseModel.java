package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.ProductModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductResponseModel extends BaseResponseModel {

    public class ProductResponseData {

        List<ProductModel> product;

        public List<ProductModel> getProduct() {
            return product;
        }

        public void setProduct(List<ProductModel> poduct) {
            this.product = poduct;
        }
    }

    ProductResponseData data;

    public ProductResponseData getData() {
        return data;
    }

    public void setData(ProductResponseData data) {
        this.data = data;
    }


}




