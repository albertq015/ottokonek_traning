package com.otto.mart.model.APIModel.Request.olshop;

import java.util.List;

public class OrderConfirmationRequestModel {
    private List<AddCartRequestModel> cart_items_attributes;

    public List<AddCartRequestModel> getCart_items_attributes() {
        return cart_items_attributes;
    }

    public void setCart_items_attributes(List<AddCartRequestModel> cart_items_attributes) {
        this.cart_items_attributes = cart_items_attributes;
    }

    public OrderConfirmationRequestModel() {
    }

    public OrderConfirmationRequestModel(List<AddCartRequestModel> cart_items_attributes) {
        this.cart_items_attributes = cart_items_attributes;
    }
}