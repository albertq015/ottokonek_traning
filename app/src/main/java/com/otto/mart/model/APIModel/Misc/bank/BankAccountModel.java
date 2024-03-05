package com.otto.mart.model.APIModel.Misc.bank;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BankAccountModel implements Parcelable {
    @JsonProperty("bank_code")
    private String bankCode;

    @JsonProperty("account_number")
    private String accountNumber;

    @JsonProperty("bank_name")
    private String bankName;

    @JsonProperty("id")
    private int id;

    @JsonProperty("bank_logo")
    private String bankLogo;

    @JsonProperty("account_owner")
    private String accountOwner;

    @JsonProperty("account_name")
    private String accountName;

    @JsonProperty("bank_pos")
    private int bankPos;

    @JsonProperty("approval_status")
    private String approvalStatus;

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("status")
    private String status;

    @JsonProperty("logo")
    private String logo;

    @JsonProperty("binName")
    private String binName;


    private int resLogo;

    public BankAccountModel() {

    }

    protected BankAccountModel(Parcel in) {
        bankCode = in.readString();
        accountNumber = in.readString();
        bankName = in.readString();
        id = in.readInt();
        bankLogo = in.readString();
        accountOwner = in.readString();
        accountName = in.readString();
        approvalStatus = in.readString();
        reason = in.readString();
        resLogo = in.readInt();
        status = in.readString();
        binName = in.readString();
        logo = in.readString();
    }

    public static final Creator<BankAccountModel> CREATOR = new Creator<BankAccountModel>() {
        @Override
        public BankAccountModel createFromParcel(Parcel in) {
            return new BankAccountModel(in);
        }

        @Override
        public BankAccountModel[] newArray(int size) {
            return new BankAccountModel[size];
        }
    };

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setBankLogo(String bankLogo) {
        this.bankLogo = bankLogo;
    }

    public String getBankLogo() {
        return bankLogo;
    }

    public void setAccountOwner(String accountOwner) {
        this.accountOwner = accountOwner;
    }

    public String getAccountOwner() {
        return accountOwner;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getBankPos() {
        return bankPos;
    }

    public void setBankPos(int bankPos) {
        this.bankPos = bankPos;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getResLogo() {
        return resLogo;
    }

    public void setResLogo(int resLogo) {
        this.resLogo = resLogo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBinName() {
        return binName;
    }

    public void setBinName(String binName) {
        this.binName = binName;
    }


    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public static Creator<BankAccountModel> getCREATOR() {
        return CREATOR;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bankCode);
        dest.writeString(accountNumber);
        dest.writeString(bankName);
        dest.writeInt(id);
        dest.writeString(bankLogo);
        dest.writeString(accountOwner);
        dest.writeString(accountName);
        dest.writeInt(bankPos);
        dest.writeString(approvalStatus);
        dest.writeString(reason);
        dest.writeInt(resLogo);
        dest.writeString(status);
        dest.writeString(binName);
        dest.writeString(logo);
    }
}
