package com.otto.mart.model.APIModel.Misc;

public class PulsaOttoagDataModel {
    private String Nama;
    private String ProductCode;
    private String Denom;
    private String SalesPrice;
    private String Company;

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }

    public String getDenom() {
        return Denom;
    }

    public void setDenom(String denom) {
        Denom = denom;
    }

    public String getSalesPrice() {
        return SalesPrice;
    }

    public void setSalesPrice(String salesPrice) {
        SalesPrice = salesPrice;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }
}
