package com.otto.mart.model.APIModel.Request.olshop;

import java.util.List;

public class ConfirmPaid{
	private String address;
	private List<AddCartRequestModel> cart_items_attributes;
	private String buyer_phone;
	private String postal_code;
	private String buyer_name;
	private String mail_no;
	private String payment_code;
	private String buyer_email;

	public String getBuyer_email() {
		return buyer_email;
	}

	public void setBuyer_email(String buyer_email) {
		this.buyer_email = buyer_email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<AddCartRequestModel> getCart_items_attributes() {
		return cart_items_attributes;
	}

	public void setCart_items_attributes(List<AddCartRequestModel> cart_items_attributes) {
		this.cart_items_attributes = cart_items_attributes;
	}

	public String getBuyer_phone() {
		return buyer_phone;
	}

	public void setBuyer_phone(String buyer_phone) {
		this.buyer_phone = buyer_phone;
	}

	public String getPostal_code() {
		return postal_code;
	}

	public void setPostal_code(String postal_code) {
		this.postal_code = postal_code;
	}

	public String getBuyer_name() {
		return buyer_name;
	}

	public void setBuyer_name(String buyer_name) {
		this.buyer_name = buyer_name;
	}

	public String getMail_no() {
		return mail_no;
	}

	public void setMail_no(String mail_no) {
		this.mail_no = mail_no;
	}

	public String getPayment_code() {
		return payment_code;
	}

	public void setPayment_code(String payment_code) {
		this.payment_code = payment_code;
	}
}