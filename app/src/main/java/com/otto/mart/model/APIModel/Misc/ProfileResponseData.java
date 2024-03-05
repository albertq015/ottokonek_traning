package com.otto.mart.model.APIModel.Misc;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.otto.mart.model.APIModel.Misc.bank.BankAccountModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfileResponseData implements Parcelable {

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("phone")
    private String phone = "-";

    @JsonProperty("secondary_phone")
    private String secondaryPhone = "-";


    @JsonProperty("latitude")
    private String latitude = "-";


    @JsonProperty("name")
    private String name = "-";


    @JsonProperty("avatar_thumb")
    private String avatarThumb = "-";


    @JsonProperty("merchant_name")
    private String merchantName = "-";


    @JsonProperty("avatar")
    private String avatar = "-";


    @JsonProperty("email")
    private String email = "";

    @JsonProperty("longitude")
    private String longitude = "-";


    @JsonProperty("merchant_id")
    private String merchantId = "-";


    @JsonProperty("dob")
    private String dob;

    @JsonProperty("business_category_name")
    private String businessCategoryName = "-";


    @JsonProperty("business_category_id")
    private int businessCategoryId;

    @JsonProperty("business_type_name")
    private String businessTypeName = "-";


    @JsonProperty("business_type_id")
    private int businessTypeId;

    @JsonProperty("addresses")
    private List<AddressModel> addresses;

    @JsonProperty("accounts")
    private List<BankAccountModel> accounts;

    public ProfileResponseData() {
    }

    public List<AddressModel> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressModel> addresses) {
        this.addresses = addresses;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSecondaryPhone() {
        return secondaryPhone;
    }

    public void setSecondaryPhone(String secondaryPhone) {
        this.secondaryPhone = secondaryPhone;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarThumb() {
        return avatarThumb;
    }

    public void setAvatarThumb(String avatarThumb) {
        this.avatarThumb = avatarThumb;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<BankAccountModel> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<BankAccountModel> accounts) {
        this.accounts = accounts;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getBusinessCategoryName() {
        return businessCategoryName;
    }

    public void setBusinessCategoryName(String businessCategoryName) {
        this.businessCategoryName = businessCategoryName;
    }

    public int getBusinessCategoryId() {
        return businessCategoryId;
    }

    public void setBusinessCategoryId(int businessCategoryId) {
        this.businessCategoryId = businessCategoryId;
    }

    public String getBusinessTypeName() {
        return businessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
        this.businessTypeName = businessTypeName;
    }

    public int getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(int businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public static Creator<ProfileResponseData> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected ProfileResponseData(Parcel in) {
        userId = in.readInt();
        phone = in.readString();
        secondaryPhone = in.readString();
        latitude = in.readString();
        name = in.readString();
        avatarThumb = in.readString();
        merchantName = in.readString();
        avatar = in.readString();
        email = in.readString();
        longitude = in.readString();
        merchantId = in.readString();
        dob = in.readString();
        businessCategoryName = in.readString();
        businessCategoryId = in.readInt();
        businessTypeName = in.readString();
        businessTypeId = in.readInt();

        addresses = in.createTypedArrayList(AddressModel.CREATOR);
        accounts = in.createTypedArrayList(BankAccountModel.CREATOR);
    }

    public static final Parcelable.Creator<ProfileResponseData> CREATOR = new Parcelable.Creator<ProfileResponseData>() {
        @Override
        public ProfileResponseData createFromParcel(Parcel in) {
            return new ProfileResponseData(in);
        }

        @Override
        public ProfileResponseData[] newArray(int size) {
            return new ProfileResponseData[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(phone);
        dest.writeString(secondaryPhone);
        dest.writeString(latitude);
        dest.writeString(name);
        dest.writeString(avatarThumb);
        dest.writeString(merchantName);
        dest.writeString(avatar);
        dest.writeString(email);
        dest.writeString(longitude);
        dest.writeString(merchantId);
        dest.writeString(dob);
        dest.writeString(businessCategoryName);
        dest.writeInt(businessCategoryId);
        dest.writeString(businessTypeName);
        dest.writeInt(businessTypeId);

        dest.writeTypedList(addresses);
        dest.writeTypedList(accounts);
    }
}
