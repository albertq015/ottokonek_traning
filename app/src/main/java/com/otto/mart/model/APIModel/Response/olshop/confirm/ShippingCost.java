package com.otto.mart.model.APIModel.Response.olshop.confirm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ShippingCost{
	private String insurance;
	private String created_at;
	private String delivery_method_code;
	private String mail_no;
	private String updated_at;
	private String price;
	private String receiver_name;
	private String receiver_address;
	private Object courier_id;
	private int id;
	private String receiver_phone;
	private String postal_code;
	private int order_id;
	private String status;

	public String getInsurance() {
		return insurance;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
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

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getReceiver_name() {
		return receiver_name;
	}

	public void setReceiver_name(String receiver_name) {
		this.receiver_name = receiver_name;
	}

	public String getReceiver_address() {
		return receiver_address;
	}

	public void setReceiver_address(String receiver_address) {
		this.receiver_address = receiver_address;
	}

	public Object getCourier_id() {
		return courier_id;
	}

	public void setCourier_id(Object courier_id) {
		this.courier_id = courier_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getReceiver_phone() {
		return receiver_phone;
	}

	public void setReceiver_phone(String receiver_phone) {
		this.receiver_phone = receiver_phone;
	}

	public String getPostal_code() {
		return postal_code;
	}

	public void setPostal_code(String postal_code) {
		this.postal_code = postal_code;
	}

	public int getOrder_id() {
		return order_id;
	}

	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
