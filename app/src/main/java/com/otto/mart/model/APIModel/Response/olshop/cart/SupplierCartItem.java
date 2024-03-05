package com.otto.mart.model.APIModel.Response.olshop.cart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Response.olshop.order.ShippingItem;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplierCartItem {
    private String image;
    private int quantity;
    private String total_price;
    private String item_price;
    private int product_id;
    private String sales_commission;
    private int cart_item_id;
    private String product_name;
    private String image_thumb;
    private boolean isSelected;
    private int parentPos;
    private String sku;
    private ShippingItem shipping;
    private String variant;
    private long ottomart_price;
    private long ottomart_discount_price;
    private String supplier_name;
    private String supplier_logo;
    private boolean insurance_required;
    private boolean insuranceCheck;
    private long price;
    private boolean is_product_price_changed;
    private String main_image_url;

    public String getMain_image_url() {
        return main_image_url;
    }

    public void setMain_image_url(String main_image_url) {
        this.main_image_url = main_image_url;
    }

    public boolean isIs_product_price_changed() {
        return is_product_price_changed;
    }

    public void setIs_product_price_changed(boolean is_product_price_changed) {
        this.is_product_price_changed = is_product_price_changed;
    }

    public boolean isInsuranceCheck() {
        return insuranceCheck;
    }

    public void setInsuranceCheck(boolean insuranceCheck) {
        this.insuranceCheck = insuranceCheck;
    }

    public boolean isInsurance_required() {
        return insurance_required;
    }

    public void setInsurance_required(boolean insurance_required) {
        this.insurance_required = insurance_required;
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

    public long getOttomart_price() {
        return ottomart_price;
    }

    public void setOttomart_price(long ottomart_price) {
        this.ottomart_price = ottomart_price;
    }

    public long getOttomart_discount_price() {
        return ottomart_discount_price;
    }

    public void setOttomart_discount_price(long ottomart_discount_price) {
        this.ottomart_discount_price = ottomart_discount_price;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public ShippingItem getShipping() {
        return shipping;
    }

    public void setShipping(ShippingItem shipping) {
        this.shipping = shipping;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getParentPos() {
        return parentPos;
    }

    public void setParentPos(int parentPos) {
        this.parentPos = parentPos;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
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

    public int getCart_item_id() {
        return cart_item_id;
    }

    public void setCart_item_id(int cart_item_id) {
        this.cart_item_id = cart_item_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getImage_thumb() {
        return image_thumb;
    }

    public void setImage_thumb(String image_thumb) {
        this.image_thumb = image_thumb;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getPrice() {
        return price;
    }
}
