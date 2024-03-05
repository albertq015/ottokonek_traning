package com.otto.mart.model.APIModel.Request.olshop.cart;

import java.util.List;

public class UpdateCartRequestModel {
    private List<UpdateItem> cart_items;

    public UpdateCartRequestModel() {
    }

    public UpdateCartRequestModel(List<UpdateItem> cart_items) {
        this.cart_items = cart_items;
    }

    public List<UpdateItem> getCart_items() {
        return cart_items;
    }

    public void setCart_items(List<UpdateItem> cart_items) {
        this.cart_items = cart_items;
    }
}
