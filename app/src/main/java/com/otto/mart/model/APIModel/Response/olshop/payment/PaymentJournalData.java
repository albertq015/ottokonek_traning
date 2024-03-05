package com.otto.mart.model.APIModel.Response.olshop.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentJournalData {
	private String response_description;
	private String reference_number;
	private String transaction_date;
	private String response_code;

	public String getResponse_description() {
		return response_description;
	}

	public void setResponse_description(String response_description) {
		this.response_description = response_description;
	}

	public String getReference_number() {
		return reference_number;
	}

	public void setReference_number(String reference_number) {
		this.reference_number = reference_number;
	}

	public String getTransaction_date() {
		return transaction_date;
	}

	public void setTransaction_date(String transaction_date) {
		this.transaction_date = transaction_date;
	}

	public String getResponse_code() {
		return response_code;
	}

	public void setResponse_code(String response_code) {
		this.response_code = response_code;
	}
}
