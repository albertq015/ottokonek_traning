package com.otto.mart.model.APIModel.Response.olshop.cart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Response.olshop.order.ShippingItem;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplierCartGroup{
	private int total_item;
	private int cart_id;
	private String sub_total;
	private boolean isSelected;
	private String supplier_name;
	private String logo;
	private List<SupplierCartItem> cart_items;
	private ShippingItem shipping;

    public ShippingItem getShipping() {
        return shipping;
    }

    public void setShipping(ShippingItem shipping) {
        this.shipping = shipping;
    }

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<SupplierCartItem> getCart_items() {
        return cart_items;
    }

    public void setCart_items(List<SupplierCartItem> cart_items) {
        this.cart_items = cart_items;
    }

    public String getSupplierName() {
        return supplier_name;
    }

    public void setSupplierName(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public int getTotal_item() {
        return total_item;
    }

    public void setTotal_item(int total_item) {
        this.total_item = total_item;
    }

    public String getSub_total() {
        return sub_total;
    }

    public void setSub_total(String sub_total) {
        this.sub_total = sub_total;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}