package com.otto.mart.model.APIModel.Request;

public class RegisterUpgradeAccountRequestModel {
    String id_photo;
    String id_card_photo;
    String product_photo;

    public String getId_photo() {
        return id_photo;
    }

    public void setId_photo(String id_photo) {
        this.id_photo = id_photo;
    }

    public String getId_card_photo() {
        return id_card_photo;
    }

    public void setId_card_photo(String id_card_photo) {
        this.id_card_photo = id_card_photo;
    }

    public String getProduct_photo() {
        return product_photo;
    }

    public void setProduct_photo(String product_photo) {
        this.product_photo = product_photo;
    }
}
