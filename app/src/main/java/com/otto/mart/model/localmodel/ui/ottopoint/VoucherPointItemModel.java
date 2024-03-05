package com.otto.mart.model.localmodel.ui.ottopoint;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VoucherPointItemModel implements Serializable {

    private String id;
    private String couponId;
    private String code;
    private String urlBanner;
    private String urlBannerDetail;
    private Drawable banner;
    private String urlCompanyPic;
    private Drawable companyPic;
    private String companyName;
    private String title;
    private String expireDate = "";
    private String usedDate = "";
    private int jumlah = 1;
    private int jumlahChild = 0; // tmp
    private boolean isExpired = false;
    private List<VoucherPointItemModel> child = new ArrayList<>();
    private String typeVoucher = ""; // voucher category type
    private String productCode = "";
    private int numberOfVoucher = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrlBanner() {
        return urlBanner;
    }

    public void setUrlBanner(String urlBanner) {
        this.urlBanner = urlBanner;
    }

    public Drawable getBanner() {
        return banner;
    }

    public void setBanner(Drawable banner) {
        this.banner = banner;
    }

    public String getUrlCompanyPic() {
        return urlCompanyPic;
    }

    public void setUrlCompanyPic(String urlCompanyPic) {
        this.urlCompanyPic = urlCompanyPic;
    }

    public Drawable getCompanyPic() {
        return companyPic;
    }

    public void setCompanyPic(Drawable companyPic) {
        this.companyPic = companyPic;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public int getJumlah() {
        return child.size();
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getUsedDate() {
        return usedDate;
    }

    public void setUsedDate(String usedDate) {
        this.usedDate = usedDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<VoucherPointItemModel> getChild() {
        return child;
    }

    public void addChild(VoucherPointItemModel child) {
        this.child.add(child);
    }

    public void setChild(List<VoucherPointItemModel> child) {
        this.child = child;
    }

    public String getUrlBannerDetail() {
        return urlBannerDetail;
    }

    public void setUrlBannerDetail(String urlBannerDetail) {
        this.urlBannerDetail = urlBannerDetail;
    }

    public int getJumlahChild() {
        return jumlahChild;
    }

    public void setJumlahChild(int jumlahChild) {
        this.jumlahChild = jumlahChild;
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

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public int getNumberOfVoucher() {
        return numberOfVoucher;
    }

    public void setNumberOfVoucher(int numberOfVoucher) {
        this.numberOfVoucher = numberOfVoucher;
    }
}
