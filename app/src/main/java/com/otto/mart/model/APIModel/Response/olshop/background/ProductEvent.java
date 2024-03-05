package com.otto.mart.model.APIModel.Response.olshop.background;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.olshop.Product;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductEvent {
    private List<Product> products;

    public ProductEvent() {
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
