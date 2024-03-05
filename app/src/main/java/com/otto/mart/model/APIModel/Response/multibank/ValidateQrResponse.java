package com.otto.mart.model.APIModel.Response.multibank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.qr.QrInquiryResponse;

import java.util.List;

public class ValidateQrResponse extends BaseResponseModel {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        private List<IssuerLinkedListItem> issuerLinkedList;
        private MerchantInfo merchantInfo;


        public List<IssuerLinkedListItem> getIssuerLinkedList() {
            return issuerLinkedList;
        }

        public void setIssuerLinkedList(List<IssuerLinkedListItem> issuerLinkedList) {
            this.issuerLinkedList = issuerLinkedList;
        }

        public MerchantInfo getMerchantInfo() {
            return merchantInfo;
        }

        public void setMerchantInfo(MerchantInfo merchantInfo) {
            this.merchantInfo = merchantInfo;
        }
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MerchantInfo {
        private String acquirerId;
        private int adminFee;
        private String currency;
        private String merchantAccount;
        private String merchantCategory;
        private String merchantCity;
        private String merchantId;
        private String merchantName;
        private String qrType;
        private String referenceLabel;
        private String requestId;
        private String tipIndicator;
        private int transactionAmount;


        public int getTransactionAmount() {
            return transactionAmount;
        }

        public void setTransactionAmount(int transactionAmount) {
            this.transactionAmount = transactionAmount;
        }




        public String getAcquirerId() {
            return acquirerId;
        }

        public void setAcquirerId(String acquirerId) {
            this.acquirerId = acquirerId;
        }

        public int getAdminFee() {
            return adminFee;
        }

        public void setAdminFee(int adminFee) {
            this.adminFee = adminFee;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getMerchantAccount() {
            return merchantAccount;
        }

        public void setMerchantAccount(String merchantAccount) {
            this.merchantAccount = merchantAccount;
        }

        public String getMerchantCategory() {
            return merchantCategory;
        }

        public void setMerchantCategory(String merchantCategory) {
            this.merchantCategory = merchantCategory;
        }

        public String getMerchantCity() {
            return merchantCity;
        }

        public void setMerchantCity(String merchantCity) {
            this.merchantCity = merchantCity;
        }

        public String getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(String merchantId) {
            this.merchantId = merchantId;
        }

        public String getMerchantName() {
            return merchantName;
        }

        public void setMerchantName(String merchantName) {
            this.merchantName = merchantName;
        }

        public String getQrType() {
            return qrType;
        }

        public void setQrType(String qrType) {
            this.qrType = qrType;
        }

        public String getReferenceLabel() {
            return referenceLabel;
        }

        public void setReferenceLabel(String referenceLabel) {
            this.referenceLabel = referenceLabel;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public String getTipIndicator() {
            return tipIndicator;
        }

        public void setTipIndicator(String tipIndicator) {
            this.tipIndicator = tipIndicator;
        }



    }

}