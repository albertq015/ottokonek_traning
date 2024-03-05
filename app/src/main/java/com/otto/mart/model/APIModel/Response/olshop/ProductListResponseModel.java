package com.otto.mart.model.APIModel.Response.olshop;

import com.otto.mart.model.APIModel.Misc.olshop.Product;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

public class ProductListResponseModel extends BaseResponseModel {

    ProductListResponseData data;

    public ProductListResponseData getData() {
        return data;
    }

    public void setData(ProductListResponseData data) {
        this.data = data;
    }

    public class ProductListResponseData{
        List<Product> products;

        public List<Product> getProducts() {
            return products;
        }

        public void setProducts(List<Product> products) {
            this.products = products;
        }
    }
}
