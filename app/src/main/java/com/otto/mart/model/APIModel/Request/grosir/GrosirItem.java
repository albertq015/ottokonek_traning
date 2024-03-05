package com.otto.mart.model.APIModel.Request.grosir;

import java.io.Serializable;

public class GrosirItem implements Serializable {
    private String product_code;
    private Double real_price;
    private  String quantity;
    private String name;
    private String url;
    private Long weight;

    public GrosirItem(String product_code, Double real_price, String quantity, String name, String url, Long weight) {
        this.product_code = product_code;
        this.real_price = real_price;
        this.quantity = quantity;
        this.name = name;
        this.url = url;
        this.weight = weight;
    }
    public GrosirItem(){}

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public Double getReal_price() {
        return real_price;
    }

    public void setReal_price(Double real_price) {
        this.real_price = real_price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
