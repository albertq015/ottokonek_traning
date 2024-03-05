package com.otto.mart.model.APIModel.Request.tokoOttopay;

public class AddToCardRequest {

    private int sku;
    private int quantity;

    public int getSku() {
        return sku;
    }

    public void setSku(int sku) {
        this.sku = sku;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
