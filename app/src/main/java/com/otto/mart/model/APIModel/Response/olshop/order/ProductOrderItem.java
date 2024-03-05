package com.otto.mart.model.APIModel.Response.olshop.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductOrderItem {
    private int quantity;
    private String total_Price;
    private String item_Price;
    private int product_id;
    private String sales_commission;
    private String sku;
    private String product_title;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTotal_Price() {
        return total_Price;
    }

    public void setTotal_Price(String total_Price) {
        this.total_Price = total_Price;
    }

    public String getItem_Price() {
        return item_Price;
    }

    public void setItem_Price(String item_Price) {
        this.item_Price = item_Price;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getSales_commission() {
        return sales_commission;
    }

    public void setSales_commission(String sales_commission) {
        this.sales_commission = sales_commission;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }
}
