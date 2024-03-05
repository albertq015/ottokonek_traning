package com.otto.mart.model.APIModel.Response;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckMerchantResponseData implements Parcelable {

    @JsonProperty("address_latitude")
    private double addressLatitude;

    @JsonProperty("village_id")
    private int villageId;

    @JsonProperty("business_category_id")
    private int businessCategoryId;

    @JsonProperty("business_category_name")
    private String businessCategoryName = "-";

    @JsonProperty("complete_address")
    private String completeAddress = "-";

    @JsonProperty("id_card")
    private String idCard = "-";

    @JsonProperty("merchant_name")
    private String merchantName = "-";

    @JsonProperty("merchant_id")
    private String merchantId = "-";

    @JsonProperty("province_id")
    private int provinceId;

    @JsonProperty("name")
    private String names = "-";

    @JsonProperty("address_longitude")
    private double addressLongitude;

    @JsonProperty("phone_number")
    private String phoneNumber = "-";

    @JsonProperty("district_id")
    private int districtId;

    @JsonProperty("city_id")
    private int cityId;

    @JsonProperty("dob")
    private long dob;

    @JsonProperty("business_location")
    private String businessLocation = "-";
    ;

    @JsonProperty("best_visit")
    private String bestVisit = "-";
    ;

    @JsonProperty("lokasi_usaha")
    private String lokasiUsaha = "-";

    @JsonProperty("business_type_name")
    private String businessTypeName = "-";

    @JsonProperty("operation_hour")
    private String operationHour = "-";

    @JsonProperty("province_name")
    private String provinceName = "-";

    @JsonProperty("city_name")
    private String cityName = "-";

    @JsonProperty("district_name")
    private String districtName = "-";

    @JsonProperty("village_name")
    private String villageName = "-";

    public CheckMerchantResponseData() {
    }

    protected CheckMerchantResponseData(Parcel in) {
        addressLatitude = in.readDouble();
        villageId = in.readInt();
        businessCategoryId = in.readInt();
        businessCategoryName = in.readString();
        completeAddress = in.readString();
        idCard = in.readString();
        merchantName = in.readString();
        merchantId = in.readString();
        provinceId = in.readInt();
        names = in.readString();
        addressLongitude = in.readDouble();
        phoneNumber = in.readString();
        districtId = in.readInt();
        cityId = in.readInt();
        dob = in.readLong();
        businessLocation = in.readString();
        bestVisit = in.readString();
        lokasiUsaha = in.readString();
        businessTypeName = in.readString();
        operationHour = in.readString();
        provinceName = in.readString();
        cityName = in.readString();
        districtName = in.readString();
        villageName = in.readString();
    }

    public static final Creator<CheckMerchantResponseData> CREATOR = new Creator<CheckMerchantResponseData>() {
        @Override
        public CheckMerchantResponseData createFromParcel(Parcel in) {
            return new CheckMerchantResponseData(in);
        }

        @Override
        public CheckMerchantResponseData[] newArray(int size) {
            return new CheckMerchantResponseData[size];
        }
    };

    public String getBusinessLocation() {
        return businessLocation;
    }

    public void setBusinessLocation(String businessLocation) {
        this.businessLocation = businessLocation;
    }

    public String getBestVisit() {
        return bestVisit;
    }

    public void setBestVisit(String bestVisit) {
        this.bestVisit = bestVisit;
    }

    public String getLokasiUsaha() {
        return lokasiUsaha;
    }

    public void setLokasiUsaha(String lokasiUsaha) {
        this.lokasiUsaha = lokasiUsaha;
    }

    public String getBusinessTypeName() {
        return businessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
        this.businessTypeName = businessTypeName;
    }

    public String getOperationHour() {
        return operationHour;
    }

    public void setOperationHour(String operationHour) {
        this.operationHour = operationHour;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public static Creator<CheckMerchantResponseData> getCREATOR() {
        return CREATOR;
    }

    public void setAddressLatitude(double addressLatitude) {
        this.addressLatitude = addressLatitude;
    }

    public double getAddressLatitude() {
        return addressLatitude;
    }

    public void setVillageId(int villageId) {
        this.villageId = villageId;
    }

    public int getVillageId() {
        return villageId;
    }

    public void setBusinessCategoryId(int businessCategoryId) {
        this.businessCategoryId = businessCategoryId;
    }

    public int getBusinessCategoryId() {
        return businessCategoryId;
    }

    public void setCompleteAddress(String completeAddress) {
        this.completeAddress = completeAddress;
    }

    public String getBusinessCategoryName() {
        return businessCategoryName;
    }

    public void setBusinessCategoryName(String businessCategoryName) {
        this.businessCategoryName = businessCategoryName;
    }

    public String getCompleteAddress() {
        return completeAddress;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setNames(String name) {
        this.names = name;
    }

    public String getNames() {
        return names;
    }

    public void setAddressLongitude(double addressLongitude) {
        this.addressLongitude = addressLongitude;
    }

    public double getAddressLongitude() {
        return addressLongitude;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getCityId() {
        return cityId;
    }

    public long getDob() {
        return dob;
    }

    public void setDob(long dob) {
        this.dob = dob;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(addressLatitude);
        dest.writeInt(villageId);
        dest.writeInt(businessCategoryId);
        dest.writeString(businessCategoryName);
        dest.writeString(completeAddress);
        dest.writeString(idCard);
        dest.writeString(merchantName);
        dest.writeString(merchantId);
        dest.writeInt(provinceId);
        dest.writeString(names);
        dest.writeDouble(addressLongitude);
        dest.writeString(phoneNumber);
        dest.writeInt(districtId);
        dest.writeInt(cityId);
        dest.writeLong(dob);
        dest.writeString(businessLocation);
        dest.writeString(bestVisit);
        dest.writeString(lokasiUsaha);
        dest.writeString(businessTypeName);
        dest.writeString(operationHour);
        dest.writeString(provinceName);
        dest.writeString(cityName);
        dest.writeString(districtName);
        dest.writeString(villageName);
    }
}