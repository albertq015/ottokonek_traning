package com.otto.mart.model.APIModel.Request.Base;

import com.otto.mart.model.APIModel.Misc.PaymentConfirmationModel;
import com.otto.mart.model.APIModel.Request.donasi.DonasiPaymentRequest;
import com.otto.mart.model.APIModel.Request.donasi.DonasiQRPaymentRequest;
import com.otto.mart.model.APIModel.Request.ppob.PpobPaymentRequest;

public class BasePpobPaymentRequestModel {

    long selling_price;
    String pin;
    int wallet_id;
    int denomination;
    String customer_reference;
    String email;
    String product_name;
    PaymentConfirmationModel confirmationModel;
    String product_code;
    String product_type;
    String period;
    String rolename;
    DonasiPaymentRequest donasiPaymentRequest;
    DonasiQRPaymentRequest donasiQRPaymentRequest;
    PpobPaymentRequest ppobPaymentRequest;

    public PaymentConfirmationModel getConfirmationModel() {
        return confirmationModel;
    }

    public void setConfirmationModel(PaymentConfirmationModel confirmationModel) {
        this.confirmationModel = confirmationModel;
    }

    public long getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(long selling_price) {
        this.selling_price = selling_price;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public int getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(int wallet_id) {
        this.wallet_id = wallet_id;
    }

    public int getDenomination() {
        return denomination;
    }

    public void setDenomination(int denomination) {
        this.denomination = denomination;
    }

    public String getCustomer_reference() {
        return customer_reference;
    }

    public void setCustomer_reference(String customer_reference) {
        this.customer_reference = customer_reference;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public DonasiPaymentRequest getDonasiPaymentRequest() {
        return donasiPaymentRequest;
    }

    public void setDonasiPaymentRequest(DonasiPaymentRequest donasiPaymentRequest) {
        this.donasiPaymentRequest = donasiPaymentRequest;
    }

    public DonasiQRPaymentRequest getDonasiQRPaymentRequest() {
        return donasiQRPaymentRequest;
    }

    public void setDonasiQRPaymentRequest(DonasiQRPaymentRequest donasiQRPaymentRequest) {
        this.donasiQRPaymentRequest = donasiQRPaymentRequest;
    }

    public PpobPaymentRequest getPpobPaymentRequest() {
        return ppobPaymentRequest;
    }

    public void setPpobPaymentRequest(PpobPaymentRequest ppobPaymentRequest) {
        this.ppobPaymentRequest = ppobPaymentRequest;
    }
}