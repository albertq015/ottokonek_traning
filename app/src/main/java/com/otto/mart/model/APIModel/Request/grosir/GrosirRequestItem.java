package com.otto.mart.model.APIModel.Request.grosir;

public class GrosirRequestItem {
    private String product_code;
    private Double real_price;
    private  int quantity;

    public GrosirRequestItem(String product_code, Double real_price, int quantity) {
        this.product_code = product_code;
        this.real_price = real_price;
        this.quantity = quantity;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public Double getReal_price() {
        return real_price;
    }

    public void setReal_price(Double real_price) {
        this.real_price = real_price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
