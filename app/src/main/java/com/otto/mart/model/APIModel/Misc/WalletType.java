package com.otto.mart.model.APIModel.Misc;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WalletType implements Parcelable{
    private int id;
    private String name;
    private String code;
    private String logo_url;

    public WalletType() {
    }

    protected WalletType(Parcel in) {
        id = in.readInt();
        name = in.readString();
        code = in.readString();
        logo_url = in.readString();
    }

    public static final Creator<WalletType> CREATOR = new Creator<WalletType>() {
        @Override
        public WalletType createFromParcel(Parcel in) {
            return new WalletType(in);
        }

        @Override
        public WalletType[] newArray(int size) {
            return new WalletType[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(code);
        dest.writeString(logo_url);
    }
}
