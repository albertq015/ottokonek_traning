package com.otto.mart.model.APIModel.Response.olshop.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CourierItem{
	private Object allowed_city;
	private String courier_name;
	private String courier_id;
	private String delivery_method_code;
	private String mail_no;

	public Object getAllowed_city() {
		return allowed_city;
	}

	public void setAllowed_city(Object allowed_city) {
		this.allowed_city = allowed_city;
	}

	public String getCourier_name() {
		return courier_name;
	}

	public void setCourier_name(String courier_name) {
		this.courier_name = courier_name;
	}

	public String getCourier_id() {
		return courier_id;
	}

	public void setCourier_id(String courier_id) {
		this.courier_id = courier_id;
	}

	public String getDelivery_method_code() {
		return delivery_method_code;
	}

	public void setDelivery_method_code(String delivery_method_code) {
		this.delivery_method_code = delivery_method_code;
	}

	public String getMail_no() {
		return mail_no;
	}

	public void setMail_no(String mail_no) {
		this.mail_no = mail_no;
	}
}
