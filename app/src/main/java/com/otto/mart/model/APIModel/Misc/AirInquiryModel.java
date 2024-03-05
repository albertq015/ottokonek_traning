package com.otto.mart.model.APIModel.Misc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AirInquiryModel {

    public String descriptionFee;
    public String strFee;
    public String strAmount;
    public String fee;
    public String bit48;
    public String description;
    public String terminalId;
    public String billerName;
    public String strDeliveryChannel;
    public String productName;
    public String responseCode;
    int total;
    String customerReference;
    int customerId;
    String billerReference;
    String merchantType;
    String referenceName;
    int amount;
    String toAccountType;
    String accountType;
    String strTransferType;
    String ToAccountNumber;
    String transactionData;
    String displayDestinationNote;
    String transactionType;
    String accountNumber;
    String transactionDate;
    String strShortTransferType;
    boolean batchProcess;
    String strTransactionDate;
    String productCode;
    String currencyCode;
    String cardNumber;
    String billPeriod;
    String billerCode;

    public String getBillerCode() {
        return billerCode;
    }

    public void setBillerCode(String billerCode) {
        this.billerCode = billerCode;
    }

    public String getDescriptionFee() {
        return descriptionFee;
    }

    public void setDescriptionFee(String descriptionFee) {
        this.descriptionFee = descriptionFee;
    }

    public String getStrFee() {
        return strFee;
    }

    public void setStrFee(String strFee) {
        this.strFee = strFee;
    }

    public String getStrAmount() {
        return strAmount;
    }

    public void setStrAmount(String strAmount) {
        this.strAmount = strAmount;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getBit48() {
        return bit48;
    }

    public void setBit48(String bit48) {
        this.bit48 = bit48;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getBillerName() {
        return billerName;
    }

    public void setBillerName(String billerName) {
        this.billerName = billerName;
    }

    public String getStrDeliveryChannel() {
        return strDeliveryChannel;
    }

    public void setStrDeliveryChannel(String strDeliveryChannel) {
        this.strDeliveryChannel = strDeliveryChannel;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getCustomerReference() {
        return customerReference;
    }

    public void setCustomerReference(String customerReference) {
        this.customerReference = customerReference;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getBillerReference() {
        return billerReference;
    }

    public void setBillerReference(String billerReference) {
        this.billerReference = billerReference;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getReferenceName() {
        return referenceName;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getToAccountType() {
        return toAccountType;
    }

    public void setToAccountType(String toAccountType) {
        this.toAccountType = toAccountType;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getStrTransferType() {
        return strTransferType;
    }

    public void setStrTransferType(String strTransferType) {
        this.strTransferType = strTransferType;
    }

    public String getToAccountNumber() {
        return ToAccountNumber;
    }

    public void setToAccountNumber(String toAccountNumber) {
        ToAccountNumber = toAccountNumber;
    }

    public String getTransactionData() {
        return transactionData;
    }

    public void setTransactionData(String transactionData) {
        this.transactionData = transactionData;
    }

    public String getDisplayDestinationNote() {
        return displayDestinationNote;
    }

    public void setDisplayDestinationNote(String displayDestinationNote) {
        this.displayDestinationNote = displayDestinationNote;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getStrShortTransferType() {
        return strShortTransferType;
    }

    public void setStrShortTransferType(String strShortTransferType) {
        this.strShortTransferType = strShortTransferType;
    }

    public boolean isBatchProcess() {
        return batchProcess;
    }

    public void setBatchProcess(boolean batchProcess) {
        this.batchProcess = batchProcess;
    }

    public String getStrTransactionDate() {
        return strTransactionDate;
    }

    public void setStrTransactionDate(String strTransactionDate) {
        this.strTransactionDate = strTransactionDate;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getBillPeriod() {
        return billPeriod;
    }

    public void setBillPeriod(String billPeriod) {
        this.billPeriod = billPeriod;
    }
}
