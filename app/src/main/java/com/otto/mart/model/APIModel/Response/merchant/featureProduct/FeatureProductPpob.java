package com.otto.mart.model.APIModel.Response.merchant.featureProduct;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FeatureProductPpob {
    String product_title;
    String product_desc;
    List<FeatureProduct> feature_product;

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public List<FeatureProduct> getFeature_product() {
        return feature_product;
    }

    public void setFeature_product(List<FeatureProduct> feature_product) {
        this.feature_product = feature_product;
    }
}
