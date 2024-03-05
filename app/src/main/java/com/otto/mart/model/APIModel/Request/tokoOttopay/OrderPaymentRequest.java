package com.otto.mart.model.APIModel.Request.tokoOttopay;

import java.util.List;

public class OrderPaymentRequest {
    private int wallet_id;
    private long amount;
    private int supplier_id;
    private String pin;
    private List<Integer> cart_item_ids;

    public int getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(int wallet_id) {
        this.wallet_id = wallet_id;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public List<Integer> getCart_item_ids() {
        return cart_item_ids;
    }

    public void setCart_item_ids(List<Integer> cart_item_ids) {
        this.cart_item_ids = cart_item_ids;
    }
}