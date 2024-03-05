package com.otto.mart.model.APIModel.Response.grosir;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import app.beelabs.com.codebase.base.response.BaseResponse;

public class DataResponseProductItem extends BaseResponse implements Serializable {

    @JsonProperty("id")
    private int id;
    @JsonProperty("images")
    private String photo;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("sell_price")
    private Double price;
    @JsonProperty("discount")
    private String discount;
    @JsonProperty("real_price")
    private Float real_price;
    @JsonProperty("stock")
    private String stock;
    private int temp_qty;
    private String temp_price;
    @JsonProperty("code")
    private String product_code;
    private Long quantity_product;
    private Long weight;

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public Long getQuantity_product() {
        return quantity_product;
    }

    public void setQuantity_product(Long quantity_product) {
        this.quantity_product = quantity_product;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public int getTemp_qty() {
        return temp_qty;
    }

    public void setTemp_qty(int temp_qty) {
        this.temp_qty = temp_qty;
    }

    public String getTemp_price() {
        return temp_price;
    }

    public void setTemp_price(String temp_price) {
        this.temp_price = temp_price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public Float getReal_price() {
        return real_price;
    }

    public void setReal_price(Float real_price) {
        this.real_price = real_price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

}
