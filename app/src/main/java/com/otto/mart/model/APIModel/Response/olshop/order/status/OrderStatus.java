package com.otto.mart.model.APIModel.Response.olshop.order.status;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderStatus{
	private Object total_item;
	private int quantity;
	private EleveniaOrderStatus elevenia_order_status;
	private String created_at;
	private String product_name;
	private List<Object> product_images;
	private int total_amount;
	private int id;
	private String supplier_name;
	private String invoice_number;
	private int supplier_id;
	private SupplierLogo supplier_logo;
	private String status;
	private String customer_ref_number;
	private int payment_id;
	private String main_image_url;

	public String getMain_image_url() {
		return main_image_url;
	}

	public void setMain_image_url(String main_image_url) {
		this.main_image_url = main_image_url;
	}

	public int getPayment_id() {
		return payment_id;
	}

	public void setPayment_id(int payment_id) {
		this.payment_id = payment_id;
	}

	public String getCustomer_ref_number() {
		return customer_ref_number;
	}

	public void setCustomer_ref_number(String customer_ref_number) {
		this.customer_ref_number = customer_ref_number;
	}

	public Object getTotal_item() {
		return total_item;
	}

	public void setTotal_item(Object total_item) {
		this.total_item = total_item;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public EleveniaOrderStatus getElevenia_order_status() {
		return elevenia_order_status;
	}

	public void setElevenia_order_status(EleveniaOrderStatus elevenia_order_status) {
		this.elevenia_order_status = elevenia_order_status;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public List<Object> getProduct_images() {
		return product_images;
	}

	public void setProduct_images(List<Object> product_images) {
		this.product_images = product_images;
	}

	public int getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(int total_amount) {
		this.total_amount = total_amount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSupplier_name() {
		return supplier_name;
	}

	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}

	public String getInvoice_number() {
		return invoice_number;
	}

	public void setInvoice_number(String invoice_number) {
		this.invoice_number = invoice_number;
	}

	public int getSupplier_id() {
		return supplier_id;
	}

	public void setSupplier_id(int supplier_id) {
		this.supplier_id = supplier_id;
	}

	public SupplierLogo getSupplier_logo() {
		return supplier_logo;
	}

	public void setSupplier_logo(SupplierLogo supplier_logo) {
		this.supplier_logo = supplier_logo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}