package com.otto.mart.model.APIModel.Misc.bank;

import android.os.Parcel;
import android.os.Parcelable;

public class BankRequestModel implements Parcelable {

    String bank_code;
    String account_number;
    String account_name;
    String approval_status;
    String reason;

    public BankRequestModel() {
    }

    protected BankRequestModel(Parcel in) {
        bank_code = in.readString();
        account_number = in.readString();
        account_name = in.readString();
        approval_status = in.readString();
        reason = in.readString();
    }

    public static final Creator<BankRequestModel> CREATOR = new Creator<BankRequestModel>() {
        @Override
        public BankRequestModel createFromParcel(Parcel in) {
            return new BankRequestModel(in);
        }

        @Override
        public BankRequestModel[] newArray(int size) {
            return new BankRequestModel[size];
        }
    };

    public String getBank_code() {
        return bank_code;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getApproval_status() {
        return approval_status;
    }

    public void setApproval_status(String approval_status) {
        this.approval_status = approval_status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason != null ? reason : "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bank_code);
        dest.writeString(account_number);
        dest.writeString(account_name);
        dest.writeString(approval_status);
        dest.writeString(reason);
    }
}
