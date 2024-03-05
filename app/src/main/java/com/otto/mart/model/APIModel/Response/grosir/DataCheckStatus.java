package com.otto.mart.model.APIModel.Response.grosir;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.otto.mart.model.APIModel.Request.grosir.ShipmentDetail;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataCheckStatus {

    private String order_no;
    private String order_date;
    private long total_amount;
    private String status_order;
    private ArrayList<ProductItem> product;
    private int supplier_id;
    private String supplier_name;
    private String payment_method;

    @JsonProperty("payment_status")
    private String paymentStatus;

    private ShipmentDetail shipment;

    public ShipmentDetail getShipment() {
        return shipment;
    }

    public void setShipment(ShipmentDetail shipment) {
        this.shipment = shipment;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
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

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public long getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(long total_amount) {
        this.total_amount = total_amount;
    }

    public String getStatus_order() {
        return status_order;
    }

    public void setStatus_order(String status_order) {
        this.status_order = status_order;
    }

    public ArrayList<ProductItem> getProduct() {
        return product;
    }

    public void setProduct(ArrayList<ProductItem> product) {
        this.product = product;
    }
}
