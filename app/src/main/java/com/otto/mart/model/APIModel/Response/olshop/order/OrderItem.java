package com.otto.mart.model.APIModel.Response.olshop.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderItem {
    private ProductOrderItem item;
    private List<ShippingItem> shipping_cost;
    private int id;
    private String invoice_number;
    private String status;

    public ProductOrderItem getItem() {
        return item;
    }

    public void setItem(ProductOrderItem item) {
        this.item = item;
    }

    public List<ShippingItem> getShipping_cost() {
        return shipping_cost;
    }

    public void setShipping_cost(List<ShippingItem> shipping_cost) {
        this.shipping_cost = shipping_cost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}