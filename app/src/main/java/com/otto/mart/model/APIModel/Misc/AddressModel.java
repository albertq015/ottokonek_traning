package com.otto.mart.model.APIModel.Misc;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressModel implements Parcelable{

    @JsonProperty("village_id")
    private long villageId;

    @JsonProperty("province_id")
    private int provinceId;

    @JsonProperty("complete_address")
    private String completeAddress;

    @JsonProperty("name")
    private String name;

    @JsonProperty("id")
    private int id;

    @JsonProperty("district_id")
    private long districtId;

    @JsonProperty("city_id")
    private int cityId;

    @JsonProperty("province_name")
    private String provinceName;

    @JsonProperty("city_name")
    private String cityName;

    @JsonProperty("district_name")
    private String districtName;

    @JsonProperty("village_name")
    private String villageName;

    @JsonProperty("province_pos")
    private String provincePos;
    @JsonProperty("city_pos")
    private String cityPos;
    @JsonProperty("district_pos")
    private String districtPos;
    @JsonProperty("village_pos")
    private String villagePos;
   @JsonProperty("status")
    private String status;
    @JsonProperty("set_as_main")
    private boolean set_as_main;

    public AddressModel() {
    }

    protected AddressModel(Parcel in) {
        villageId = in.readLong();
        provinceId = in.readInt();
        completeAddress = in.readString();
        name = in.readString();
        id = in.readInt();
        districtId = in.readLong();
        cityId = in.readInt();
        provinceName = in.readString();
        cityName = in.readString();
        districtName = in.readString();
        villageName = in.readString();
        provincePos = in.readString();
        cityPos = in.readString();
        districtPos = in.readString();
        villagePos = in.readString();
        status = in.readString();
        set_as_main = in.readByte() != 0;
    }

    public static final Creator<AddressModel> CREATOR = new Creator<AddressModel>() {
        @Override
        public AddressModel createFromParcel(Parcel in) {
            return new AddressModel(in);
        }

        @Override
        public AddressModel[] newArray(int size) {
            return new AddressModel[size];
        }
    };

    public boolean isSet_as_main() {
        return set_as_main;
    }

    public void setSet_as_main(boolean set_as_main) {
        this.set_as_main = set_as_main;
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

    public String getProvincePos() {
        return provincePos;
    }

    public void setProvincePos(String provincePos) {
        this.provincePos = provincePos;
    }

    public String getCityPos() {
        return cityPos;
    }

    public void setCityPos(String cityPos) {
        this.cityPos = cityPos;
    }

    public String getDistrictPos() {
        return districtPos;
    }

    public void setDistrictPos(String districtPos) {
        this.districtPos = districtPos;
    }

    public String getVillagePos() {
        return villagePos;
    }

    public void setVillagePos(String villagePos) {
        this.villagePos = villagePos;
    }

    public long getVillageId() {
        return villageId;
    }

    public void setVillageId(long villageId) {
        this.villageId = villageId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setCompleteAddress(String completeAddress) {
        this.completeAddress = completeAddress;
    }

    public String getCompleteAddress() {
        return completeAddress;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(long districtId) {
        this.districtId = districtId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getCityId() {
        return cityId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(villageId);
        dest.writeInt(provinceId);
        dest.writeString(completeAddress);
        dest.writeString(name);
        dest.writeInt(id);
        dest.writeLong(districtId);
        dest.writeInt(cityId);
        dest.writeString(provinceName);
        dest.writeString(cityName);
        dest.writeString(districtName);
        dest.writeString(villageName);
        dest.writeString(provincePos);
        dest.writeString(cityPos);
        dest.writeString(districtPos);
        dest.writeString(villagePos);
        dest.writeString(status);
        dest.writeByte((byte) (set_as_main ? 1 : 0));
    }
}