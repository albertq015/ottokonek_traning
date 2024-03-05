package com.otto.mart.model.APIModel.Response.ottopoint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpHistoryTransactionResponseModel extends BaseResponseOttopoint {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data{

        private List<Transfers> transfers;

        public List<Transfers> getTransfers() {
            return transfers;
        }

        public void setTransfers(List<Transfers> transfers) {
            this.transfers = transfers;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Transfers{

            private String pointsTransferId;
            private String accountId;
            private String customerId;
            private String customerFirstName;
            private String customerLastName;
            private String customerEmail;
            private String createdAt;
            private String expiresAt;
            private long value;
            private String state;
            private String type;
            private String comment;
            private String issuer;

            public String getPointsTransferId() {
                return pointsTransferId;
            }

            public void setPointsTransferId(String pointsTransferId) {
                this.pointsTransferId = pointsTransferId;
            }

            public String getAccountId() {
                return accountId;
            }

            public void setAccountId(String accountId) {
                this.accountId = accountId;
            }

            public String getCustomerId() {
                return customerId;
            }

            public void setCustomerId(String customerId) {
                this.customerId = customerId;
            }

            public String getCustomerFirstName() {
                return customerFirstName;
            }

            public void setCustomerFirstName(String customerFirstName) {
                this.customerFirstName = customerFirstName;
            }

            public String getCustomerLastName() {
                return customerLastName;
            }

            public void setCustomerLastName(String customerLastName) {
                this.customerLastName = customerLastName;
            }

            public String getCustomerEmail() {
                return customerEmail;
            }

            public void setCustomerEmail(String customerEmail) {
                this.customerEmail = customerEmail;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getExpiresAt() {
                return expiresAt;
            }

            public void setExpiresAt(String expiresAt) {
                this.expiresAt = expiresAt;
            }

            public long getValue() {
                return value;
            }

            public void setValue(long value) {
                this.value = value;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getComment() {
                return comment;
            }

            public void setComment(String comment) {
                this.comment = comment;
            }

            public String getIssuer() {
                return issuer;
            }

            public void setIssuer(String issuer) {
                this.issuer = issuer;
            }
        }
    }
}
