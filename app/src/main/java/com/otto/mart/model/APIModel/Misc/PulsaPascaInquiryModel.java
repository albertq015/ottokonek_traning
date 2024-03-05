package com.otto.mart.model.APIModel.Misc;

public class PulsaPascaInquiryModel extends PulsaInquiryModel {

    String bit48;
    int fee;
    int total;
    String strTransactionDate;
    String customerId;
    String bit56;
    String billerReference;
    String referenceName;


    public String getBit48() {
        return bit48;
    }

    public void setBit48(String bit48) {
        this.bit48 = bit48;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getStrTransactionDate() {
        return strTransactionDate;
    }

    public void setStrTransactionDate(String strTransactionDate) {
        this.strTransactionDate = strTransactionDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getBit56() {
        return bit56;
    }

    public void setBit56(String bit56) {
        this.bit56 = bit56;
    }

    public String getBillerReference() {
        return billerReference;
    }

    public void setBillerReference(String billerReference) {
        this.billerReference = billerReference;
    }

    public String getReferenceName() {
        return referenceName;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
    }
}
