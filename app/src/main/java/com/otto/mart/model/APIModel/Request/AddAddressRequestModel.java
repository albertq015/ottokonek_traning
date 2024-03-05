package com.otto.mart.model.APIModel.Request;

public class AddAddressRequestModel {
	private long village_id;
	private long province_id;
	private String complete_address;
	private String name;
	private long district_id;
	private long city_id;

	public long getVillage_id() {
		return village_id;
	}

	public void setVillage_id(long village_id) {
		this.village_id = village_id;
	}

	public long getProvince_id() {
		return province_id;
	}

	public void setProvince_id(long province_id) {
		this.province_id = province_id;
	}

	public String getComplete_address() {
		return complete_address;
	}

	public void setComplete_address(String complete_address) {
		this.complete_address = complete_address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getDistrict_id() {
		return district_id;
	}

	public void setDistrict_id(long district_id) {
		this.district_id = district_id;
	}

	public long getCity_id() {
		return city_id;
	}

	public void setCity_id(long city_id) {
		this.city_id = city_id;
	}
}
