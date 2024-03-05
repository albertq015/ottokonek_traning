package com.otto.mart.model.APIModel.Misc.tokoOttopay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Cart {
    private int cart_item_id;
    private int product_id;
    private String sku;
    private String product_name;
    private boolean insurance_required;
    private String image;
    private String image_thumb;
    private int item_price;
    private int quantity;
    private int supplier_id;
    private String supplier_name;
    private String supplier_logo;
    private String total_price;
    private int ottomart_price;
    private int ottomart_discount_price;
    private boolean isSelected;

    public int getCart_item_id() {
        return cart_item_id;
    }

    public void setCart_item_id(int cart_item_id) {
        this.cart_item_id = cart_item_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public boolean isInsurance_required() {
        return insurance_required;
    }

    public void setInsurance_required(boolean insurance_required) {
        this.insurance_required = insurance_required;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_thumb() {
        return image_thumb;
    }

    public void setImage_thumb(String image_thumb) {
        this.image_thumb = image_thumb;
    }

    public int getItem_price() {
        return item_price;
    }

    public void setItem_price(int item_price) {
        this.item_price = item_price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getSupplier_logo() {
        return supplier_logo;
    }

    public void setSupplier_logo(String supplier_logo) {
        this.supplier_logo = supplier_logo;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public int getOttomart_price() {
        return ottomart_price;
    }

    public void setOttomart_price(int ottomart_price) {
        this.ottomart_price = ottomart_price;
    }

    public int getOttomart_discount_price() {
        return ottomart_discount_price;
    }

    public void setOttomart_discount_price(int ottomart_discount_price) {
        this.ottomart_discount_price = ottomart_discount_price;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}