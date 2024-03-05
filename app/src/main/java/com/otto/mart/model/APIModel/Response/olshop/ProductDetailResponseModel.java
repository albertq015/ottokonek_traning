package com.otto.mart.model.APIModel.Response.olshop;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDetailResponseModel extends BaseResponseModel {

    private ProductDetailResponseData data;

    public ProductDetailResponseData getData() {
        return data;
    }

    public void setData(ProductDetailResponseData data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class ProductDetailResponseData {
        private ProductDetail product;

        public ProductDetail getProduct() {
            return product;
        }

        public void setProduct(ProductDetail product) {
            this.product = product;
        }
    }
}