package com.otto.mart.model.APIModel.Response.olshop.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.olshop.Variant;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EveleniaData{
	private String description;
	private int original_sale_price;
	private String selling_status_name;
	private String sell_limit_quantity;
	private int product_id;
	private String prodcut_url;
	private long stock;
	private String sku;
	private String brand;
	private String selling_status;
	private List<Object> additional_images;
	private String sell_limit_type_code;
	private String sell_min_limit_quantity;
	private String image_url;
	private int weight;
	private String incld_prd;
	private String product_name;
	private String average_delivery_duration;
	private String seller_store_name;
	private String level3_category_id;
	private String level2_category_id;
	private List<CourierItem> courier;
	private int delivery_template_sequence;
	private String sell_min_limit_type_code;
	private int discounted_product_price;
	private String level1_categoryId;
	private List<Variant> option;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getOriginal_sale_price() {
		return original_sale_price;
	}

	public void setOriginal_sale_price(int original_sale_price) {
		this.original_sale_price = original_sale_price;
	}

	public String getSelling_status_name() {
		return selling_status_name;
	}

	public void setSelling_status_name(String selling_status_name) {
		this.selling_status_name = selling_status_name;
	}

	public String getSell_limit_quantity() {
		return sell_limit_quantity;
	}

	public void setSell_limit_quantity(String sell_limit_quantity) {
		this.sell_limit_quantity = sell_limit_quantity;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public String getProdcut_url() {
		return prodcut_url;
	}

	public void setProdcut_url(String prodcut_url) {
		this.prodcut_url = prodcut_url;
	}

	public long getStock() {
		return stock;
	}

	public void setStock(long stock) {
		this.stock = stock;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getSelling_status() {
		return selling_status;
	}

	public void setSelling_status(String selling_status) {
		this.selling_status = selling_status;
	}

	public List<Object> getAdditional_images() {
		return additional_images;
	}

	public void setAdditional_images(List<Object> additional_images) {
		this.additional_images = additional_images;
	}

	public String getSell_limit_type_code() {
		return sell_limit_type_code;
	}

	public void setSell_limit_type_code(String sell_limit_type_code) {
		this.sell_limit_type_code = sell_limit_type_code;
	}

	public String getSell_min_limit_quantity() {
		return sell_min_limit_quantity;
	}

	public void setSell_min_limit_quantity(String sell_min_limit_quantity) {
		this.sell_min_limit_quantity = sell_min_limit_quantity;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String getIncld_prd() {
		return incld_prd;
	}

	public void setIncld_prd(String incld_prd) {
		this.incld_prd = incld_prd;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getAverage_delivery_duration() {
		return average_delivery_duration;
	}

	public void setAverage_delivery_duration(String average_delivery_duration) {
		this.average_delivery_duration = average_delivery_duration;
	}

	public String getSeller_store_name() {
		return seller_store_name;
	}

	public void setSeller_store_name(String seller_store_name) {
		this.seller_store_name = seller_store_name;
	}

	public String getLevel3_category_id() {
		return level3_category_id;
	}

	public void setLevel3_category_id(String level3_category_id) {
		this.level3_category_id = level3_category_id;
	}

	public String getLevel2_category_id() {
		return level2_category_id;
	}

	public void setLevel2_category_id(String level2_category_id) {
		this.level2_category_id = level2_category_id;
	}

	public List<CourierItem> getCourier() {
		return courier;
	}

	public void setCourier(List<CourierItem> courier) {
		this.courier = courier;
	}

	public int getDelivery_template_sequence() {
		return delivery_template_sequence;
	}

	public void setDelivery_template_sequence(int delivery_template_sequence) {
		this.delivery_template_sequence = delivery_template_sequence;
	}

	public String getSell_min_limit_type_code() {
		return sell_min_limit_type_code;
	}

	public void setSell_min_limit_type_code(String sell_min_limit_type_code) {
		this.sell_min_limit_type_code = sell_min_limit_type_code;
	}

	public int getDiscounted_product_price() {
		return discounted_product_price;
	}

	public void setDiscounted_product_price(int discounted_product_price) {
		this.discounted_product_price = discounted_product_price;
	}

	public String getLevel1_categoryId() {
		return level1_categoryId;
	}

	public void setLevel1_categoryId(String level1_categoryId) {
		this.level1_categoryId = level1_categoryId;
	}

	public List<Variant> getOption() {
		return option;
	}

	public void setOption(List<Variant> option) {
		this.option = option;
	}
}