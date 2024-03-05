package com.otto.mart.model.APIModel.Misc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthDataModel implements Serializable {

    int user_id;
    String access_token;
    String merchant_id;
    String name;
    String merchant_name;
    String email;
    int need_otp;
    String avatar;
    String avatar_thumb;
    String latitude;
    String longitude;
    String phone;
    String secondary_phone;
    String omset_balance;
    String wallet_balance;
    int wallet_id;
    int daily_omset;
    String business_category_name;
    String business_type_name;
    String status;
    String address;
    String addressProvince;
    String addressCity;
    String addressDistrict;
    String addressVillage;
    boolean acceptOcbiTnc;
    String mpan;
    String mid;
    String nmid;
    String tid;
    Long addressCityId;
    Long addressVillageId;
    Long addressDistrictId;
    String memberType;
    String category;
    String merchantGroupName;
    String business_category_id;
    String bin;
    String binName;
    String account_number;
    String addressBarangayCode;
    String addressProvinceCode;
    String addressRegion;
    String addressMunicipality;
    String addressBarangay;



    public String getBinName() {
        return binName;
    }

    public void setBinName(String binName) {
        this.binName = binName;
    }

    public String getAddressRegion() {
        return addressRegion;
    }

    public void setAddressRegion(String addressRegion) {
        this.addressRegion = addressRegion;
    }

    public String getAddressMunicipality() {
        return addressMunicipality;
    }

    public void setAddressMunicipality(String addressMunicipality) {
        this.addressMunicipality = addressMunicipality;
    }

    public String getAddressBarangay() {
        return addressBarangay;
    }

    public void setAddressBarangay(String addressBarangay) {
        this.addressBarangay = addressBarangay;
    }

    public String getAddressProvinceCode() {
        return addressProvinceCode;
    }

    public void setAddressProvinceCode(String addressProvinceCode) {
        this.addressProvinceCode = addressProvinceCode;
    }

    public String getAddressBarangayCode() {
        return addressBarangayCode;
    }

    public void setAddressBarangayCode(String addressBarangayCode) {
        this.addressBarangayCode = addressBarangayCode;
    }

    public String getBusiness_category_id() {
        return business_category_id;
    }

    public void setBusiness_category_id(String business_category_id) {
        this.business_category_id = business_category_id;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public Long getAddressVillageId() {
        return addressVillageId;
    }

    public void setAddressVillageId(Long addressVillageId) {
        this.addressVillageId = addressVillageId;
    }

    public Long getAddressDistrictId() {
        return addressDistrictId;
    }

    public void setAddressDistrictId(Long addressDistrictId) {
        this.addressDistrictId = addressDistrictId;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getNmid() {
        return nmid;
    }

    public void setNmid(String nmid) {
        this.nmid = nmid;
    }

    public boolean isAcceptOcbiTnc() {
        return acceptOcbiTnc;
    }

    public void setAcceptOcbiTnc(boolean acceptOcbiTnc) {
        this.acceptOcbiTnc = acceptOcbiTnc;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMpan() {
        return mpan;
    }

    public void setMpan(String mpan) {
        this.mpan = mpan;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNeed_otp() {
        return need_otp;
    }

    public void setNeed_otp(int need_otp) {
        this.need_otp = need_otp;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar_thumb() {
        return avatar_thumb;
    }

    public void setAvatar_thumb(String avatar_thumb) {
        this.avatar_thumb = avatar_thumb;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSecondary_phone() {
        return secondary_phone;
    }

    public void setSecondary_phone(String secondary_phone) {
        this.secondary_phone = secondary_phone;
    }

    public String getOmset_balance() {
        return omset_balance;
    }

    public void setOmset_balance(String omset_balance) {
        this.omset_balance = omset_balance;
    }

    public String getWallet_balance() {
        return wallet_balance;
    }

    public void setWallet_balance(String wallet_balance) {
        this.wallet_balance = wallet_balance;
    }

    public int getDaily_omset() {
        return daily_omset;
    }

    public void setDaily_omset(int daily_omset) {
        this.daily_omset = daily_omset;
    }

    public int getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(int wallet_id) {
        this.wallet_id = wallet_id;
    }

    public String getBusiness_category_name() {
        return business_category_name;
    }

    public void setBusiness_category_name(String business_category_name) {
        this.business_category_name = business_category_name;
    }

    public String getBusiness_type_name() {
        return business_type_name;
    }

    public void setBusiness_type_name(String business_type_name) {
        this.business_type_name = business_type_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getAddressCityId() {
        return addressCityId;
    }

    public void setAddressCityId(Long addressCityId) {
        this.addressCityId = addressCityId;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMerchantGroupName() {
        return merchantGroupName;
    }

    public void setMerchantGroupName(String merchantGroupName) {
        this.merchantGroupName = merchantGroupName;
    }
}