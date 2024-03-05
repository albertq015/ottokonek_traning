package com.otto.mart.model.APIModel.Response.olshop.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ShippingItem{
	private String courier_name;
	private String insurance_cost;
	private String shipping_cost;
	private String product_id;
	private String courier_id;
	private String delivery_method_code;
	private String sku;

	public String getCourier_name() {
		return courier_name;
	}

	public void setCourier_name(String courier_name) {
		this.courier_name = courier_name;
	}

	public String getInsurance_cost() {
		return insurance_cost;
	}

	public void setInsurance_cost(String insurance_cost) {
		this.insurance_cost = insurance_cost;
	}

	public String getShipping_cost() {
		return shipping_cost;
	}

	public void setShipping_cost(String shipping_cost) {
		this.shipping_cost = shipping_cost;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
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

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}
}
