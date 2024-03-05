package com.otto.mart.model.localmodel.ui.ottopoint;

import java.io.Serializable;

public class DealsItemModel implements Serializable {

    private String id;
    private String url_img_banner;
    private String url_img_banner_detail;
    private int url_img_banner_sample;
    private String url_img_company_logo;
    private int url_img_company_logo_sample;
    private String company_name;
    private String title;
    private String price_text;
    private long price;
    private long jumlahVoucherAvailable = 0;
    private String typeVoucher = ""; // voucher category type
    private String productCode = "";

    public DealsItemModel(String id, String url_img_banner, String url_img_banner_detail, String url_img_company_logo, String company_name, String title, String price_text, long price){
        this.id = id;
        this.url_img_banner = url_img_banner;
        this.url_img_banner_detail = url_img_banner_detail;
        this.url_img_company_logo = url_img_company_logo;
        this.company_name = company_name;
        this.title = title;
        this.price_text = price_text;
        this.price = price;
    }

    public DealsItemModel(String id, int url_img_banner_sample, int url_img_company_logo_sample, String company_name, String title, String price_text, long price){
        this.id = id;
        this.url_img_banner_sample = url_img_banner_sample;
        this.url_img_company_logo_sample = url_img_company_logo_sample;
        this.company_name = company_name;
        this.title = title;
        this.price_text = price_text;
        this.price = price;
    }

    public String getUrl_img_banner() {
        return url_img_banner;
    }

    public void setUrl_img_banner(String url_img_banner) {
        this.url_img_banner = url_img_banner;
    }

    public String getUrl_img_company_logo() {
        return url_img_company_logo;
    }

    public void setUrl_img_company_logo(String url_img_company_logo) {
        this.url_img_company_logo = url_img_company_logo;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice_text() {
        return price_text;
    }

    public void setPrice_text(String price_text) {
        this.price_text = price_text;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUrl_img_banner_sample() {
        return url_img_banner_sample;
    }

    public void setUrl_img_banner_sample(int url_img_banner_sample) {
        this.url_img_banner_sample = url_img_banner_sample;
    }

    public int getUrl_img_company_logo_sample() {
        return url_img_company_logo_sample;
    }

    public void setUrl_img_company_logo_sample(int url_img_company_logo_sample) {
        this.url_img_company_logo_sample = url_img_company_logo_sample;
    }

    public long getJumlahVoucherAvailable() {
        return jumlahVoucherAvailable;
    }

    public void setJumlahVoucherAvailable(long jumlahVoucherAvailable) {
        this.jumlahVoucherAvailable = jumlahVoucherAvailable;
    }

    public String getUrl_img_banner_detail() {
        return url_img_banner_detail;
    }

    public void setUrl_img_banner_detail(String url_img_banner_detail) {
        this.url_img_banner_detail = url_img_banner_detail;
    }

    public String getTypeVoucher() {
        return typeVoucher;
    }

    public void setTypeVoucher(String typeVoucher) {
        this.typeVoucher = typeVoucher;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
}
