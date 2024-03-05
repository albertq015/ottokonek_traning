package com.otto.mart.model.APIModel.Request.olshop;

public class DeleteCartRequestModel {
    private int cart_item_id;

    public DeleteCartRequestModel(int cart_item_id) {
        this.cart_item_id = cart_item_id;
    }

    public DeleteCartRequestModel() {
    }

    public int getCart_item_id() {
        return cart_item_id;
    }

    public void setCart_item_id(int cart_item_id) {
        this.cart_item_id = cart_item_id;
    }
}
