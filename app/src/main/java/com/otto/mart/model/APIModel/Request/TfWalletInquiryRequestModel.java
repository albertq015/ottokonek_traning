package com.otto.mart.model.APIModel.Request;

public class TfWalletInquiryRequestModel {
	private String wallet_channel_code;
	private long amount;
	private String customer_reference;
	private String description;

	public String getWallet_channel_code() {
		return wallet_channel_code;
	}

	public void setWallet_channel_code(String wallet_channel_code) {
		this.wallet_channel_code = wallet_channel_code;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public String getCustomer_reference() {
		return customer_reference;
	}

	public void setCustomer_reference(String customer_reference) {
		this.customer_reference = customer_reference;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
