package com.otto.mart.model.APIModel.Request.olshop;

public class AddCartRequestModel {
    private int quantity;
    private int product_id;
    private long sales_commission;
    private String sku;
    private int id;
    private String delivery_method_code;
    private long shipping_cost;
    private long insurance_cost;
    private String variant;
    private boolean insurance_required;

    public AddCartRequestModel() {
    }

    public AddCartRequestModel(int quantity, int product_id, long sales_commission, String sku) {
        this.quantity = quantity;
        this.product_id = product_id;
        this.sales_commission = sales_commission;
        this.sku = sku;
    }

    public AddCartRequestModel(int quantity, int product_id, long sales_commission, String sku, int id) {
        this.quantity = quantity;
        this.product_id = product_id;
        this.sales_commission = sales_commission;
        this.sku = sku;
        this.id = id;
    }

    public boolean isInsurance_required() {
        return insurance_required;
    }

    public void setInsurance_required(boolean insurance_required) {
        this.insurance_required = insurance_required;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getDelivery_method_code() {
        return delivery_method_code;
    }

    public void setDelivery_method_code(String delivery_method_code) {
        this.delivery_method_code = delivery_method_code;
    }

    public long getShipping_cost() {
        return shipping_cost;
    }

    public void setShipping_cost(long shipping_cost) {
        this.shipping_cost = shipping_cost;
    }

    public long getInsurance_cost() {
        return insurance_cost;
    }

    public void setInsurance_cost(long insurance_cost) {
        this.insurance_cost = insurance_cost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public void setSales_commission(long sales_commission) {
        this.sales_commission = sales_commission;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getProduct_id() {
        return product_id;
    }

    public long getSales_commission() {
        return sales_commission;
    }
}
