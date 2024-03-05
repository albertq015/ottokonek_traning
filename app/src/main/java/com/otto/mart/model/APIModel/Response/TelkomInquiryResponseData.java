package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TelkomInquiryResponseData{

	@JsonProperty("amount")
	private int amount;

	@JsonProperty("total")
	private int total;

	@JsonProperty("productCode")
	private String productCode;

	@JsonProperty("billPeriodsJson")
	private String billPeriodsJson;

	@JsonProperty("customerReference")
	private String customerReference;

	@JsonProperty("bit48")
	private String bit48;

	@JsonProperty("fee")
	private int fee;

	@JsonProperty("customerId")
	private String customerId;

	@JsonProperty("bit56")
	private String bit56;

	@JsonProperty("accountNumber")
	private String accountNumber;

	@JsonProperty("billerCode")
	private String billerCode;

	@JsonProperty("referenceName")
	private String referenceName;

	public void setAmount(int amount){
		this.amount = amount;
	}

	public int getAmount(){
		return amount;
	}

	public void setTotal(int total){
		this.total = total;
	}

	public int getTotal(){
		return total;
	}

	public void setProductCode(String productCode){
		this.productCode = productCode;
	}

	public String getProductCode(){
		return productCode;
	}

	public void setBillPeriodsJson(String billPeriodsJson){
		this.billPeriodsJson = billPeriodsJson;
	}

	public String getBillPeriodsJson(){
		return billPeriodsJson;
	}

	public void setCustomerReference(String customerReference){
		this.customerReference = customerReference;
	}

	public String getCustomerReference(){
		return customerReference;
	}

	public void setBit48(String bit48){
		this.bit48 = bit48;
	}

	public String getBit48(){
		return bit48;
	}

	public void setFee(int fee){
		this.fee = fee;
	}

	public int getFee(){
		return fee;
	}

	public void setCustomerId(String customerId){
		this.customerId = customerId;
	}

	public String getCustomerId(){
		return customerId;
	}

	public void setBit56(String bit56){
		this.bit56 = bit56;
	}

	public String getBit56(){
		return bit56;
	}

	public void setAccountNumber(String accountNumber){
		this.accountNumber = accountNumber;
	}

	public String getAccountNumber(){
		return accountNumber;
	}

	public void setBillerCode(String billerCode){
		this.billerCode = billerCode;
	}

	public String getBillerCode(){
		return billerCode;
	}

	public void setReferenceName(String referenceName){
		this.referenceName = referenceName;
	}

	public String getReferenceName(){
		return referenceName;
	}
}