package com.otto.mart.model.APIModel.Response.olshop.cart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CartItem {
    private int cart_id;
    private String supplier_name;
    private String sub_total;
    private List<Map<String, SupplierCartGroup>> cart_items;

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getSub_total() {
        return sub_total;
    }

    public void setSub_total(String sub_total) {
        this.sub_total = sub_total;
    }

    public List<Map<String, SupplierCartGroup>> getCart_items() {
        return cart_items;
    }

    public void setCart_items(List<Map<String, SupplierCartGroup>> cart_items) {
        this.cart_items = cart_items;
    }
}
