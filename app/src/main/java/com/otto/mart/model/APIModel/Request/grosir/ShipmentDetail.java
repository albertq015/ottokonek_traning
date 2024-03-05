package com.otto.mart.model.APIModel.Request.grosir;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ShipmentDetail implements Serializable {
    private String recipient_name;
    private String recipient_phone;
    private String recipient_email;
    private String recipient_province_name;
    private String recipient_city_name;
    private String recipient_district_name;
    private String recipient_address_name;
    private String recipient_address_detail;
    private long recipient_zip_code;
    private String courier_code;
    private String courier_name;
    private String courier_service;
    private String courier_description;
    private long courier_weight;
    private Double courier_cost;

    public String getRecipient_name() {
        return recipient_name;
    }

    public void setRecipient_name(String recipient_name) {
        this.recipient_name = recipient_name;
    }

    public String getRecipient_phone() {
        return recipient_phone;
    }

    public void setRecipient_phone(String recipient_phone) {
        this.recipient_phone = recipient_phone;
    }

    public String getRecipient_email() {
        return recipient_email;
    }

    public void setRecipient_email(String recipient_email) {
        this.recipient_email = recipient_email;
    }

    public String getRecipient_province_name() {
        return recipient_province_name;
    }

    public void setRecipient_province_name(String recipient_province_name) {
        this.recipient_province_name = recipient_province_name;
    }

    public String getRecipient_city_name() {
        return recipient_city_name;
    }

    public void setRecipient_city_name(String recipient_city_name) {
        this.recipient_city_name = recipient_city_name;
    }

    public String getRecipient_district_name() {
        return recipient_district_name;
    }

    public void setRecipient_district_name(String recipient_district_name) {
        this.recipient_district_name = recipient_district_name;
    }

    public String getRecipient_address_name() {
        return recipient_address_name;
    }

    public void setRecipient_address_name(String recipient_address_name) {
        this.recipient_address_name = recipient_address_name;
    }

    public String getRecipient_address_detail() {
        return recipient_address_detail;
    }

    public void setRecipient_address_detail(String recipient_address_detail) {
        this.recipient_address_detail = recipient_address_detail;
    }

    public long getRecipient_zip_code() {
        return recipient_zip_code;
    }

    public void setRecipient_zip_code(long recipient_zip_code) {
        this.recipient_zip_code = recipient_zip_code;
    }

    public String getCourier_code() {
        return courier_code;
    }

    public void setCourier_code(String courier_code) {
        this.courier_code = courier_code;
    }

    public String getCourier_name() {
        return courier_name;
    }

    public void setCourier_name(String courier_name) {
        this.courier_name = courier_name;
    }

    public String getCourier_service() {
        return courier_service;
    }

    public void setCourier_service(String courier_service) {
        this.courier_service = courier_service;
    }

    public String getCourier_description() {
        return courier_description;
    }

    public void setCourier_description(String courier_description) {
        this.courier_description = courier_description;
    }

    public long getCourier_weight() {
        return courier_weight;
    }

    public void setCourier_weight(long courier_weight) {
        this.courier_weight = courier_weight;
    }

    public Double getCourier_cost() {
        return courier_cost;
    }

    public void setCourier_cost(Double courier_cost) {
        this.courier_cost = courier_cost;
    }
}
