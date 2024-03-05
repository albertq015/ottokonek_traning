package com.otto.mart.model.APIModel.Request.olshop.cart;

public class UpdateItem {
    private int cart_item_id;
    private int quantity;

    public UpdateItem() {
    }

    public UpdateItem(int cart_item_id, int quantity) {
        this.cart_item_id = cart_item_id;
        this.quantity = quantity;
    }

    public int getCart_item_id() {
        return cart_item_id;
    }

    public void setCart_item_id(int cart_item_id) {
        this.cart_item_id = cart_item_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
