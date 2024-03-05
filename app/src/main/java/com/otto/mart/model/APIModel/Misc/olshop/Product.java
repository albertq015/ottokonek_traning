package com.otto.mart.model.APIModel.Misc.olshop;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    private String image;
    private int price;
    private int discount;
    private int id;
    private String title;
    private String image_thumb;
    private long ottomart_price;
    private long ottomart_discount_price;
    private String main_image_url;
    private int discount_percentage;

    public int getDiscount_percentage() {
        return discount_percentage;
    }

    public Product setDiscount_percentage(int discount_percentage) {
        this.discount_percentage = discount_percentage;
        return this;
    }

    public String getMain_image_url() {
        return main_image_url;
    }

    public void setMain_image_url(String main_image_url) {
        this.main_image_url = main_image_url;
    }

    public long getOttomart_price() {
        return ottomart_price;
    }

    public void setOttomart_price(long ottomart_price) {
        this.ottomart_price = ottomart_price;
    }

    public long getOttomart_discount_price() {
        return ottomart_discount_price;
    }

    public void setOttomart_discount_price(long ottomart_discount_price) {
        this.ottomart_discount_price = ottomart_discount_price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getDiscount() {
        return discount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getImage_thumb() {
        return image_thumb;
    }

    public void setImage_thumb(String image_thumb) {
        this.image_thumb = image_thumb;
    }
}
