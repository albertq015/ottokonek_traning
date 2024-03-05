package com.otto.mart.model.APIModel.Response.olshop.confirm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrdersItem{
	private Item item;
	private ShippingCost shipping_cost;
	private int id;
	private int supplier_id;
	private String invoice_number;
	private String status;

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public ShippingCost getShipping_cost() {
		return shipping_cost;
	}

	public void setShipping_cost(ShippingCost shipping_cost) {
		this.shipping_cost = shipping_cost;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSupplier_id() {
		return supplier_id;
	}

	public void setSupplier_id(int supplier_id) {
		this.supplier_id = supplier_id;
	}

	public String getInvoice_number() {
		return invoice_number;
	}

	public void setInvoice_number(String invoice_number) {
		this.invoice_number = invoice_number;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
