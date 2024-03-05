package com.otto.mart.model.APIModel.Response.merchant.featureProduct;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FeatureProductData {

    private List<FeatureProductPpob> ppob;
    private List<FeatureProductPpob> product_finansial;
    private List<FeatureProductPpob> toko_grosir;
    private List<FeatureProductPpob> toko_online;

    public List<FeatureProductPpob> getPpob() {
        return ppob;
    }

    public void setPpob(List<FeatureProductPpob> ppob) {
        this.ppob = ppob;
    }

    public List<FeatureProductPpob> getProduct_finansial() {
        return product_finansial;
    }

    public void setProduct_finansial(List<FeatureProductPpob> product_finansial) {
        this.product_finansial = product_finansial;
    }

    public List<FeatureProductPpob> getToko_grosir() {
        return toko_grosir;
    }

    public void setToko_grosir(List<FeatureProductPpob> toko_grosir) {
        this.toko_grosir = toko_grosir;
    }

    public List<FeatureProductPpob> getToko_online() {
        return toko_online;
    }

    public void setToko_online(List<FeatureProductPpob> toko_online) {
        this.toko_online = toko_online;
    }
}
