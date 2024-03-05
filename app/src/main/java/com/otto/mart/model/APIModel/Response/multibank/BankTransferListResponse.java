package com.otto.mart.model.APIModel.Response.multibank;

import com.otto.mart.model.APIModel.Response.BaseModel.RoseBaseResponseModel;

import java.util.List;

public class BankTransferListResponse extends RoseBaseResponseModel {


    private List<Data> data;


    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }


    public static class Data {
        private String accountNumber;
        private String bankCode;
        private String createdAt;
        private String id;

        private String mid;
        private String notes;
        private String pushNotifData;
        private String pushNotifStatus;
        private String status;
        private String updatedAt;
        private String updatedBy;
        private String accountName;
        private String logo;
        private String bankName;


        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }


        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }



        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getBankCode() {
            return bankCode;
        }

        public void setBankCode(String bankCode) {
            this.bankCode = bankCode;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public String getPushNotifData() {
            return pushNotifData;
        }

        public void setPushNotifData(String pushNotifData) {
            this.pushNotifData = pushNotifData;
        }

        public String getPushNotifStatus() {
            return pushNotifStatus;
        }

        public void setPushNotifStatus(String pushNotifStatus) {
            this.pushNotifStatus = pushNotifStatus;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getUpdatedBy() {
            return updatedBy;
        }

        public void setUpdatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
        }


    }

}
