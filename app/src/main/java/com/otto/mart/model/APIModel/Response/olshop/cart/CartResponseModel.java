package com.otto.mart.model.APIModel.Response.olshop.cart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

public class CartResponseModel extends BaseResponseModel {

    private AddCartResponseData data;
    private FailedCart faileds;

    public FailedCart getFaileds() {
        return faileds;
    }

    public void setFaileds(FailedCart faileds) {
        this.faileds = faileds;
    }

    public AddCartResponseData getData() {
        return data;
    }

    public void setData(AddCartResponseData data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class AddCartResponseData {
        private List<SupplierCartGroup> cart;

        public List<SupplierCartGroup> getCart() {
            return cart;
        }

        public void setCart(List<SupplierCartGroup> cart) {
            this.cart = cart;
        }
    }
}
