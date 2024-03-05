package com.otto.mart.model.APIModel.Request.tokoOttopay;

import java.util.List;

public class OrderConfirmRequest {
    private int supplier_id;
    private List<Integer> cart_item_ids;

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public List<Integer> getCart_item_ids() {
        return cart_item_ids;
    }

    public void setCart_item_ids(List<Integer> cart_item_ids) {
        this.cart_item_ids = cart_item_ids;
    }
}