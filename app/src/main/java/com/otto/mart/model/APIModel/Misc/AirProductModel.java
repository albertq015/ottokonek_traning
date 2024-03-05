package com.otto.mart.model.APIModel.Misc;

public class AirProductModel {
    String biller_code;
    String product_code;
    String product_name;
    String customer_reference;
    String logo;

    public String getBiller_code() {
        return biller_code;
    }

    public void setBiller_code(String biller_code) {
        this.biller_code = biller_code;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProduct_name() {
        if (product_name.equalsIgnoreCase("Air Palyja"))
            return "Palyja";
        else
            return product_name;
    }

    public void setProduct_name(String getProduct_name) {
        this.product_name = getProduct_name;
    }

    public String getCustomer_reference() {
        return customer_reference;
    }

    public void setCustomer_reference(String customer_reference) {
        this.customer_reference = customer_reference;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
