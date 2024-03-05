package com.otto.mart.model.APIModel.Response.grosir;

import java.io.Serializable;

public class ProductItem implements Serializable {
    private long total_amount;
    private int quantity;
    private ProductDetail product_details;

    public long getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(long total_amount) {
        this.total_amount = total_amount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ProductDetail getProduct_details() {
        return product_details;
    }

    public void setProduct_details(ProductDetail product_details) {
        this.product_details = product_details;
    }
}
