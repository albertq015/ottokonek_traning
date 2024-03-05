package com.otto.mart.model.APIModel.Request.tokoOttopay;

public class RemoveCardRequest {
    private int cart_item_id;

    public int getCart_item_id() {
        return cart_item_id;
    }

    public void setCart_item_id(int cart_item_id) {
        this.cart_item_id = cart_item_id;
    }
}