package com.otto.mart.model.APIModel.Response.olshop.confirm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Item{
	private int quantity;
	private String total_price;
	private String item_price;
	private int product_id;
	private String sales_commission;
	private String supplier_name;
	private String sku;
	private int supplier_id;
	private String product_title;
	private SupplierLogo supplier_logo;
	private Double ottomart_price;
	private Double ottomart_discount_price;

	public Double getOttomart_price() {
		return ottomart_price;
	}

	public void setOttomart_price(Double ottomart_price) {
		this.ottomart_price = ottomart_price;
	}

	public Double getOttomart_discount_price() {
		return ottomart_discount_price;
	}

	public void setOttomart_discount_price(Double ottomart_discount_price) {
		this.ottomart_discount_price = ottomart_discount_price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getTotal_price() {
		return total_price;
	}

	public void setTotal_price(String total_price) {
		this.total_price = total_price;
	}

	public String getItem_price() {
		return item_price;
	}

	public void setItem_price(String item_price) {
		this.item_price = item_price;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public String getSales_commission() {
		return sales_commission;
	}

	public void setSales_commission(String sales_commission) {
		this.sales_commission = sales_commission;
	}

	public String getSupplier_name() {
		return supplier_name;
	}

	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public int getSupplier_id() {
		return supplier_id;
	}

	public void setSupplier_id(int supplier_id) {
		this.supplier_id = supplier_id;
	}

	public String getProduct_title() {
		return product_title;
	}

	public void setProduct_title(String product_title) {
		this.product_title = product_title;
	}

	public SupplierLogo getSupplier_logo() {
		return supplier_logo;
	}

	public void setSupplier_logo(SupplierLogo supplier_logo) {
		this.supplier_logo = supplier_logo;
	}
}
