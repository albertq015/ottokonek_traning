package com.otto.mart.model.APIModel.Request.grosir;

import java.util.ArrayList;

public class GrosirRegisterSupplierRequest {
    private int supplier_id;
    private String merchant_name;
    private String mid;
    private String address;
    private String contact_person;
    private ArrayList<String> doc_labels;
    private ArrayList<String> doc_images;

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact_person() {
        return contact_person;
    }

    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }

    public ArrayList<String> getDoc_labels() {
        return doc_labels;
    }

    public void setDoc_labels(ArrayList<String> doc_labels) {
        this.doc_labels = doc_labels;
    }

    public ArrayList<String> getDoc_images() {
        return doc_images;
    }

    public void setDoc_images(ArrayList<String> doc_images) {
        this.doc_images = doc_images;
    }
}
