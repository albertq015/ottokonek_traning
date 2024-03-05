package com.otto.mart.model.APIModel.Response.olshop.confirm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Payment{
	private String total_amount;
	private String total_paid;
	private String subtotal_product;
	private List<OrdersItem> orders;
	private int id;
	private String promo_id;
	private String subtotal_shipment;
	private String status;

	public String getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}

	public String getTotal_paid() {
		return total_paid;
	}

	public void setTotal_paid(String total_paid) {
		this.total_paid = total_paid;
	}

	public String getSubtotal_product() {
		return subtotal_product;
	}

	public void setSubtotal_product(String subtotal_product) {
		this.subtotal_product = subtotal_product;
	}

	public List<OrdersItem> getOrders() {
		return orders;
	}

	public void setOrders(List<OrdersItem> orders) {
		this.orders = orders;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPromo_id() {
		return promo_id;
	}

	public void setPromo_id(String promo_id) {
		this.promo_id = promo_id;
	}

	public String getSubtotal_shipment() {
		return subtotal_shipment;
	}

	public void setSubtotal_shipment(String subtotal_shipment) {
		this.subtotal_shipment = subtotal_shipment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}