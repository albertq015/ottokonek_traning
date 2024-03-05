package com.otto.mart.model.APIModel.Request.register

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class SignUpRequest(
		@JsonProperty("account_number") val account_number: String? = null,
		@JsonProperty("business_categoryId") val business_categoryId: Int? = null,
		@JsonProperty("pin_confirmation") val pin_confirmation: String? = null,
		@JsonProperty("bin") val bin: String? = null,
		@JsonProperty("complete_address") val complete_address: String? = null,
		@JsonProperty("latitude") val latitude: Double? = null,
		@JsonProperty("municipality_code") val municipality_code: String? = null,
		@JsonProperty("barangay_code") val barangay_code: String? = null,
		@JsonProperty("merchant_name") val merchant_name: String? = null,
		@JsonProperty("merchant_id") val merchant_id: String? = null,
		@JsonProperty("firebase_token") val firebase_token: String? = null,
		@JsonProperty("province_code") val province_code: String? = null,
		@JsonProperty("indomarco_status") val indomarco_status: Boolean = false,
		@JsonProperty("pin") val pin: String? = null,
		@JsonProperty("phone") val phone: String? = null,
		@JsonProperty("dob") val dob: Int? = null,
		@JsonProperty("name") val name: String? = null,
		@JsonProperty("referal_number") val referal_number: String? = null,
		@JsonProperty("longitude") val longitude: Double? = null,
		@JsonProperty("region_code") val region_code: String? = null
)

