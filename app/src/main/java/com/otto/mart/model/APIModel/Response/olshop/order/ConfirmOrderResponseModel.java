package com.otto.mart.model.APIModel.Response.olshop.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.cart.SupplierCartItem;

import java.util.List;

public class ConfirmOrderResponseModel extends BaseResponseModel {

    private ConfirmOrderData data;

    public ConfirmOrderData getData() {
        return data;
    }

    public void setData(ConfirmOrderData data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class ConfirmOrderData {
        private List<SupplierCartItem> cart_items;

        public List<SupplierCartItem> getCart_items() {
            return cart_items;
        }

        public void setCart_items(List<SupplierCartItem> cart_items) {
            this.cart_items = cart_items;
        }
    }
}
