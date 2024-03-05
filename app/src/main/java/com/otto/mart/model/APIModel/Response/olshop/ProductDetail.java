package com.otto.mart.model.APIModel.Response.olshop;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Response.olshop.product.EveleniaData;
import com.otto.mart.model.APIModel.Response.olshop.product.Product;
import com.otto.mart.model.APIModel.Response.olshop.product.Supplier;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDetail {
    private List<ImagesItem> images;
    private Breadcrumb breadcrumb;
    private int price;
    private int discount;
    private String  description;
    private int id;
    private String title;
    private EveleniaData elevenia_data;
    private Product details;
    private Supplier supplier;
    private long ottomart_price;
    private long ottomart_discount_price;

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

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Product getDetails() {
        return details;
    }

    public void setDetails(Product details) {
        this.details = details;
    }


    public List<ImagesItem> getImages() {
        return images;
    }

    public void setImages(List<ImagesItem> images) {
        this.images = images;
    }

    public Breadcrumb getBreadcrumb() {
        return breadcrumb;
    }

    public void setBreadcrumb(Breadcrumb breadcrumb) {
        this.breadcrumb = breadcrumb;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public EveleniaData getElevenia_data() {
        return elevenia_data;
    }

    public void setElevenia_data(EveleniaData elevenia_data) {
        this.elevenia_data = elevenia_data;
    }
}
