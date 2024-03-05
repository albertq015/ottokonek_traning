package com.otto.mart.model.APIModel.Misc;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Keep;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OmzetHistoryResponseData implements Parcelable {
    @JsonProperty("merchant_name")
    private long merchantName;

    @JsonProperty("date")
    private long date;

    @JsonProperty("date_string")
    private String dateString;

    @JsonProperty("amount")
    private String amount;

    @JsonProperty("description")
    private String description;

    @JsonProperty("type")
    private String type;

    @JsonProperty("reference_number")
    private String referenceNumber;

    @JsonProperty("status")
    private String status;

    @JsonProperty("direction")
    private String direction;

    @JsonProperty("payment_method")
    private String method;

    @JsonProperty("biller_reference")
    private String billerReference;

    @JsonProperty("stroom_token")
    private String stroomToken;

    @JsonProperty("customer_name")
    private String customerName;

    @JsonProperty("commission")
    private String commission;

    @JsonProperty("serial")
    private String serial;

    @JsonProperty("pinkode")
    private String pinCode;

    @JsonProperty("payment_id")
    private int paymentId = -1;

    @Keep
    public OmzetHistoryResponseData() {

    }

    protected OmzetHistoryResponseData(Parcel in) {
        date = in.readLong();
        merchantName = in.readLong();
        dateString = in.readString();
        amount = in.readString();
        description = in.readString();
        type = in.readString();
        referenceNumber = in.readString();
        status = in.readString();
        direction = in.readString();
        method = in.readString();
        billerReference = in.readString();
        stroomToken = in.readString();
        customerName = in.readString();
        commission = in.readString();
        serial = in.readString();
        pinCode = in.readString();
        paymentId = in.readInt();
    }

    public static final Creator<OmzetHistoryResponseData> CREATOR = new Creator<OmzetHistoryResponseData>() {
        @Override
        public OmzetHistoryResponseData createFromParcel(Parcel in) {
            return new OmzetHistoryResponseData(in);
        }

        @Override
        public OmzetHistoryResponseData[] newArray(int size) {
            return new OmzetHistoryResponseData[size];
        }
    };

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getDate() {
        return date;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getBillerReference() {
        return billerReference;
    }

    public void setBillerReference(String billerReference) {
        this.billerReference = billerReference;
    }

    public String getStroomToken() {
        return stroomToken;
    }

    public void setStroomToken(String stroomToken) {
        this.stroomToken = stroomToken;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(date);
        dest.writeString(dateString);
        dest.writeString(amount);
        dest.writeString(description);
        dest.writeString(type);
        dest.writeString(referenceNumber);
        dest.writeString(status);
        dest.writeString(direction);
        dest.writeString(method);
        dest.writeString(billerReference);
        dest.writeString(stroomToken);
        dest.writeString(customerName);
        dest.writeString(commission);
        dest.writeString(serial);
        dest.writeString(pinCode);
        dest.writeInt(paymentId);
    }
}
