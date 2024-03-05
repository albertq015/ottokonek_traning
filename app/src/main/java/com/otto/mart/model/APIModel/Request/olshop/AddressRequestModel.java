package com.otto.mart.model.APIModel.Request.olshop;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressRequestModel implements Parcelable {
    private int id;
    private String mail_no;
    private String province;
    private String city;
    private String district;
    private String address;
    private String addressName;
    private String postalCode;
    private long districtId;
    private long cityId;
    private int[] state;
    private String municipality_code;

    public String getMunicipality_code() {
        return municipality_code;
    }

    public void setMunicipality_code(String municipality_code) {
        this.municipality_code = municipality_code;
    }

    public AddressRequestModel() {
    }

    protected AddressRequestModel(Parcel in) {
        province = in.readString();
        city = in.readString();
        district = in.readString();
        address = in.readString();
        addressName = in.readString();
        postalCode = in.readString();
        state = in.createIntArray();
        districtId = in.readLong();
        cityId = in.readLong();
        municipality_code = in.readString();
    }

    public static final Creator<AddressRequestModel> CREATOR = new Creator<AddressRequestModel>() {
        @Override
        public AddressRequestModel createFromParcel(Parcel in) {
            return new AddressRequestModel(in);
        }

        @Override
        public AddressRequestModel[] newArray(int size) {
            return new AddressRequestModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(province);
        dest.writeString(city);
        dest.writeString(district);
        dest.writeString(address);
        dest.writeString(addressName);
        dest.writeString(postalCode);
        dest.writeIntArray(state);
        dest.writeLong(districtId);
        dest.writeLong(cityId);
        dest.writeString(municipality_code);
    }

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMail_no() {
        return mail_no;
    }

    public void setMail_no(String mail_no) {
        this.mail_no = mail_no;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public static Creator<AddressRequestModel> getCREATOR() {
        return CREATOR;
    }

    public void setState(int[] state) {
        this.state = state;
    }

    public int[] getState() {
        return state;
    }

    public long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(long districtId) {
        this.districtId = districtId;
    }
}
