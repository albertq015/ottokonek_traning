package com.otto.mart.model.APIModel.Misc;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WalletDataModel implements Parcelable {
    int id;
    String owner_name;
    String account_number;
    String logo;
    String balance;
    String wallet_type_name;

    protected WalletDataModel(Parcel in) {
        id = in.readInt();
        owner_name = in.readString();
        account_number = in.readString();
        logo = in.readString();
        balance = in.readString();
        wallet_type_name = in.readString();
    }

    public WalletDataModel() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(owner_name);
        dest.writeString(account_number);
        dest.writeString(logo);
        dest.writeString(balance);
        dest.writeString(wallet_type_name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WalletDataModel> CREATOR = new Creator<WalletDataModel>() {
        @Override
        public WalletDataModel createFromParcel(Parcel in) {
            return new WalletDataModel(in);
        }

        @Override
        public WalletDataModel[] newArray(int size) {
            return new WalletDataModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getWallet_type_name() {
        return wallet_type_name;
    }

    public void setWallet_type_name(String wallet_type_name) {
        this.wallet_type_name = wallet_type_name;
    }
}