package com.otto.mart.model.APIModel.Misc.olshop;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Variant{
	private String name;
	private String stock;
	private String sku;
	private String value;
	private List<Variant> child;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<Variant> getChild() {
		return child;
	}

	public void setChild(List<Variant> child) {
		this.child = child;
	}
}