package com.otto.mart.model.APIModel.Response.ottocash;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusOttocashData {
	private String verifyStatus;
	private String responseDescription;
	private String accountType;
	private String currency;
	private String accountNumber;
	private String birthDate;
	private String emoneyBalance;
	private String customerName;
	private String responseCode;
	private String status;

	public void setVerifyStatus(String verifyStatus){
		this.verifyStatus = verifyStatus;
	}

	public String getVerifyStatus(){
		return verifyStatus;
	}

	public void setResponseDescription(String responseDescription){
		this.responseDescription = responseDescription;
	}

	public String getResponseDescription(){
		return responseDescription;
	}

	public void setAccountType(String accountType){
		this.accountType = accountType;
	}

	public String getAccountType(){
		return accountType;
	}

	public void setCurrency(String currency){
		this.currency = currency;
	}

	public String getCurrency(){
		return currency;
	}

	public void setAccountNumber(String accountNumber){
		this.accountNumber = accountNumber;
	}

	public String getAccountNumber(){
		return accountNumber;
	}

	public void setBirthDate(String birthDate){
		this.birthDate = birthDate;
	}

	public String getBirthDate(){
		return birthDate;
	}

	public void setEmoneyBalance(String emoneyBalance){
		this.emoneyBalance = emoneyBalance;
	}

	public String getEmoneyBalance(){
		return emoneyBalance;
	}

	public void setCustomerName(String customerName){
		this.customerName = customerName;
	}

	public String getCustomerName(){
		return customerName;
	}

	public void setResponseCode(String responseCode){
		this.responseCode = responseCode;
	}

	public String getResponseCode(){
		return responseCode;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}
