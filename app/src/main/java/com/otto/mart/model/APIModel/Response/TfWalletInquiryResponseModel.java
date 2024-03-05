package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TfWalletInquiryResponseModel extends BaseResponseModel{

	private TfWalletConfResponseData data;

	public TfWalletConfResponseData getData() {
		return data;
	}

	public void setData(TfWalletConfResponseData data) {
		this.data = data;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public class TfWalletConfResponseData{
		private int amount;
		private Object periodType;
		private int endDate;
		private String receiverName;
		private String customerReference;
		private String description;
		private Object transferType;
		private String receiverNickname;
		private int valueDate;
		private String accountNumber;
		private Object periodValue;

		public void setAmount(int amount){
			this.amount = amount;
		}

		public int getAmount(){
			return amount;
		}

		public void setPeriodType(Object periodType){
			this.periodType = periodType;
		}

		public Object getPeriodType(){
			return periodType;
		}

		public void setEndDate(int endDate){
			this.endDate = endDate;
		}

		public int getEndDate(){
			return endDate;
		}

		public void setReceiverName(String receiverName){
			this.receiverName = receiverName;
		}

		public String getReceiverName(){
			return receiverName;
		}

		public void setCustomerReference(String customerReference){
			this.customerReference = customerReference;
		}

		public String getCustomerReference(){
			return customerReference;
		}

		public void setDescription(String description){
			this.description = description;
		}

		public String getDescription(){
			return description;
		}

		public void setTransferType(Object transferType){
			this.transferType = transferType;
		}

		public Object getTransferType(){
			return transferType;
		}

		public void setReceiverNickname(String receiverNickname){
			this.receiverNickname = receiverNickname;
		}

		public String getReceiverNickname(){
			return receiverNickname;
		}

		public void setValueDate(int valueDate){
			this.valueDate = valueDate;
		}

		public int getValueDate(){
			return valueDate;
		}

		public void setAccountNumber(String accountNumber){
			this.accountNumber = accountNumber;
		}

		public String getAccountNumber(){
			return accountNumber;
		}

		public void setPeriodValue(Object periodValue){
			this.periodValue = periodValue;
		}

		public Object getPeriodValue(){
			return periodValue;
		}
	}
}
