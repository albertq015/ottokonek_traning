package com.otto.mart.model.APIModel.Request.olshop;

public class ShippingCostRequestModel{
	private int product_id;
	private int qty;
	private String sku;
	private String mail_no;

	public ShippingCostRequestModel() {
	}

	public ShippingCostRequestModel(int product_id, int qty, String sku, String mail_no) {
		this.product_id = product_id;
		this.qty = qty;
		this.sku = sku;
		this.mail_no = mail_no;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getMail_no() {
		return mail_no;
	}

	public void setMail_no(String mail_no) {
		this.mail_no = mail_no;
	}
}
