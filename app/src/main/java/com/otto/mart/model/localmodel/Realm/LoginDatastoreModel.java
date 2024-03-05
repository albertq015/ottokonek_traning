package com.otto.mart.model.localmodel.Realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LoginDatastoreModel extends RealmObject {

    @PrimaryKey
    Integer SessionKey;
    Integer user_id;
    String access_token;
    String merchant_id;
    String name;
    String merchant_name;
    String email;
    String avatar;
    String avatar_thumb;
    String phone;
    String secondary_phone;
    String omset_balance;
    String wallet_balance;
    Integer daily_omset;
    Integer wallet_id;
    String business_category_name;
    String address;
    String addressProvince;
    String addressCity;
    String addressDistrict;
    String addressVillage;
    String mpan;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressProvince() {
        return addressProvince;
    }

    public void setAddressProvince(String addressProvince) {
        this.addressProvince = addressProvince;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressDistrict() {
        return addressDistrict;
    }

    public void setAddressDistrict(String addressDistrict) {
        this.addressDistrict = addressDistrict;
    }

    public String getAddressVillage() {
        return addressVillage;
    }

    public void setAddressVillage(String addressVillage) {
        this.addressVillage = addressVillage;
    }

    public String getMpan() {
        return mpan;
    }

    public void setMpan(String mpan) {
        this.mpan = mpan;
    }

    public Integer getSessionKey() {
        return SessionKey;
    }

    public void setSessionKey(Integer sessionKey) {
        SessionKey = sessionKey;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setAvatar_thumb(String avatar_thumb) {
        this.avatar_thumb = avatar_thumb;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setSecondary_phone(String secondary_phone) {
        this.secondary_phone = secondary_phone;
    }

    public void setOmset_balance(String omset_balance) {
        this.omset_balance = omset_balance;
    }

    public void setWallet_balance(String wallet_balance) {
        this.wallet_balance = wallet_balance;
    }

    public void setDaily_omset(Integer daily_omset) {
        this.daily_omset = daily_omset;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public String getName() {
        return name;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getAvatar_thumb() {
        return avatar_thumb;
    }

    public String getPhone() {
        return phone;
    }

    public String getSecondary_phone() {
        return secondary_phone;
    }

    public String getOmset_balance() {
        return omset_balance;
    }

    public String getWallet_balance() {
        return wallet_balance;
    }

    public Integer getDaily_omset() {
        return daily_omset;
    }

    public Integer getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(Integer wallet_id) {
        this.wallet_id = wallet_id;
    }

    public String getBusiness_category_name() {
        return business_category_name;
    }

    public void setBusiness_category_name(String business_category_name) {
        this.business_category_name = business_category_name;
    }
}
