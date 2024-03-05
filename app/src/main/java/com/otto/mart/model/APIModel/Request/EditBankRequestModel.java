package com.otto.mart.model.APIModel.Request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EditBankRequestModel{

	@JsonProperty("bank_account_id")
	private int bankAccountId;

	@JsonProperty("account_number")
	private String accountNumber;

	@JsonProperty("bank_code")
	private int bankId;

	@JsonProperty("account_owner")
	private String accountOwner;

	public void setBankAccountId(int bankAccountId){
		this.bankAccountId = bankAccountId;
	}

	public int getBankAccountId(){
		return bankAccountId;
	}

	public void setAccountNumber(String accountNumber){
		this.accountNumber = accountNumber;
	}

	public String getAccountNumber(){
		return accountNumber;
	}

	public void setBankId(int bankId){
		this.bankId = bankId;
	}

	public int getBankId(){
		return bankId;
	}

	public void setAccountOwner(String accountOwner){
		this.accountOwner = accountOwner;
	}

	public String getAccountOwner(){
		return accountOwner;
	}
}