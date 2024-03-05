package com.otto.mart.model.localmodel.ui;

public class HeaderModel extends BasicUIModel {
    private String shopName;
    private String productName;
    private String ownerName;
    private String shopId;
    private int imageId;

    public HeaderModel(String shopName, String productName, String ownerName, String shopId, int imageId) {
        this.shopName = shopName;
        this.productName = productName;
        this.ownerName = ownerName;
        this.shopId = shopId;
        this.imageId = imageId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
