package com.otto.mart.model.APIModel.Response.olshop.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.olshop.Variant;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    private List<CourierItem> couriers;
    private String image_url;
    private List<Variant> options;
    private int weight;
    private String description;
    private int discounted_product_price;
    private String product_name;
    private int original_sale_price;
    private String sell_min_limit_quantity;
    private String sell_limit_quantity;
    private String sku;

    public int getSell_min_limit_quantity() {
        if (sell_min_limit_quantity != null && !sell_min_limit_quantity.isEmpty()) {
            return Integer.parseInt(sell_min_limit_quantity);
        }
        return 0;
    }

    public void setSell_min_limit_quantity(String sell_min_limit_quantity) {
        this.sell_min_limit_quantity = sell_min_limit_quantity;
    }

    public int getSell_limit_quantity() {
        if (sell_limit_quantity != null && !sell_limit_quantity.isEmpty()) {
            return Integer.parseInt(sell_limit_quantity);
        }
        return 0;
    }

    public void setSell_limit_quantity(String sell_limit_quantity) {
        this.sell_limit_quantity = sell_limit_quantity;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public List<CourierItem> getCouriers() {
        return couriers;
    }

    public void setCouriers(List<CourierItem> couriers) {
        this.couriers = couriers;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public List<Variant> getOptions() {
        return options;
    }

    public void setOptions(List<Variant> options) {
        this.options = options;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDiscounted_product_price() {
        return discounted_product_price;
    }

    public void setDiscounted_product_price(int discounted_product_price) {
        this.discounted_product_price = discounted_product_price;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getOriginal_sale_price() {
        return original_sale_price;
    }

    public void setOriginal_sale_price(int original_sale_price) {
        this.original_sale_price = original_sale_price;
    }
}