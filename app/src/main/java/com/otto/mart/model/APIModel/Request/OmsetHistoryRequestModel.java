package com.otto.mart.model.APIModel.Request;

public class OmsetHistoryRequestModel {
    private int page;
    private int item_per_page;
    private int wallet_id;

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public int getItem_per_page() {
        return item_per_page;
    }

    public void setItem_per_page(int item_per_page) {
        this.item_per_page = item_per_page;
    }

    public int getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(int wallet_id) {
        this.wallet_id = wallet_id;
    }
}
