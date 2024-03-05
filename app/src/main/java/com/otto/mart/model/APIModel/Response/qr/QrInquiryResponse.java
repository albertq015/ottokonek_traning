package com.otto.mart.model.APIModel.Response.qr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QrInquiryResponse extends BaseResponseModel {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {

        private String requestId;
        private String qrType;
        private String merchantCategory;
        private String merchantName;
        private String merchantCity;
        private String merchantPostalCode;
        private String currency;
        private String billNumber;
        private String storeLabel;
        private int adminFee;
        private String tipIndicator;
        private double transactionAmount;
        private int tipFeeFixed;
        private int tipFeePercentage;

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public String getQrType() {
            return qrType;
        }

        public void setQrType(String qrType) {
            this.qrType = qrType;
        }

        public String getMerchantCategory() {
            return merchantCategory;
        }

        public void setMerchantCategory(String merchantCategory) {
            this.merchantCategory = merchantCategory;
        }

        public String getMerchantName() {
            return merchantName;
        }

        public void setMerchantName(String merchantName) {
            this.merchantName = merchantName;
        }

        public String getMerchantCity() {
            return merchantCity;
        }

        public void setMerchantCity(String merchantCity) {
            this.merchantCity = merchantCity;
        }

        public String getMerchantPostalCode() {
            return merchantPostalCode;
        }

        public void setMerchantPostalCode(String merchantPostalCode) {
            this.merchantPostalCode = merchantPostalCode;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getBillNumber() {
            return billNumber;
        }

        public void setBillNumber(String billNumber) {
            this.billNumber = billNumber;
        }

        public String getStoreLabel() {
            return storeLabel;
        }

        public void setStoreLabel(String storeLabel) {
            this.storeLabel = storeLabel;
        }

        public int getAdminFee() {
            return adminFee;
        }

        public void setAdminFee(int adminFee) {
            this.adminFee = adminFee;
        }

        public String getTipIndicator() {
            return tipIndicator;
        }

        public void setTipIndicator(String tipIndicator) {
            this.tipIndicator = tipIndicator;
        }

        public double getTransactionAmount() {
            return transactionAmount;
        }

        public void setTransactionAmount(double transactionAmount) {
            this.transactionAmount = transactionAmount;
        }

        public int getTipFeeFixed() {
            return tipFeeFixed;
        }

        public void setTipFeeFixed(int tipFeeFixed) {
            this.tipFeeFixed = tipFeeFixed;
        }

        public int getTipFeePercentage() {
            return tipFeePercentage;
        }

        public void setTipFeePercentage(int tipFeePercentage) {
            this.tipFeePercentage = tipFeePercentage;
        }
    }
}
