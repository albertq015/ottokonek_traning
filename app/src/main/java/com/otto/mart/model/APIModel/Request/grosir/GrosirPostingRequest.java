package com.otto.mart.model.APIModel.Request.grosir;

import java.util.ArrayList;

public class GrosirPostingRequest{

    private int supplier_id;
    private String merchant_phone;
    private String supplier_name;
    private ArrayList<GrosirRequestItem> items;
    private ShipmentDetail shipment;
    private String payment_source;
    private String payment_method;
    private String delivery_method_name;
    private String delivery_method;

    private String payment_type;

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getPayment_source() {
        return payment_source;
    }

    public void setPayment_source(String payment_source) {
        this.payment_source = payment_source;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public ShipmentDetail getShipment() {
        return shipment;
    }


    public String getDelivery_method_name() {
        return delivery_method_name;
    }

    public void setDelivery_method_name(String delivery_method_name) {
        this.delivery_method_name = delivery_method_name;
    }

    public String getDelivery_method() {
        return delivery_method;
    }

    public void setDelivery_method(String delivery_method) {
        this.delivery_method = delivery_method;
    }

    public void setShipment(ShipmentDetail shipment) {
        this.shipment = shipment;
    }

    public String getMerchant_phone() {
        return merchant_phone;
    }

    public void setMerchant_phone(String merchant_phone) {
        this.merchant_phone = merchant_phone;
    }

    /**
     * Gets supplier id.
     *
     * @return the supplier id
     */
    public int getSupplier_id() {
        return supplier_id;
    }

    /**
     * Sets supplier id.
     *
     * @param supplier_id the supplier id
     */
    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }


    public ArrayList<GrosirRequestItem> getItems() {
        return items;
    }

    /**
     * Sets items.
     *
     * @param items the items
     */
    public void setItems(ArrayList<GrosirRequestItem> items) {
        this.items = items;
    }

    /**
     * The type Item supplier model.
     */
}
