package com.otto.mart.model.APIModel.Response.olshop.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentJournal{
	private String order_invoice;
	private PaymentJournalData payment_journal_data;

	public String getOrder_invoice() {
		return order_invoice;
	}

	public void setOrder_invoice(String order_invoice) {
		this.order_invoice = order_invoice;
	}

	public PaymentJournalData getPayment_journal_data() {
		return payment_journal_data;
	}

	public void setPayment_journal_data(PaymentJournalData payment_journal_data) {
		this.payment_journal_data = payment_journal_data;
	}
}
