package com.otto.mart.model.APIModel.Request.bogasari

import com.fasterxml.jackson.annotation.JsonProperty

data class BogasariInquiryRequestModel(

	@field:JsonProperty("walletid")
	val walletid: Int? = null,

	@field:JsonProperty("datetime")
	val datetime: String? = null,

	@field:JsonProperty("amount")
	val amount: Long? = null,

	@field:JsonProperty("merchant_id")
	val merchantId: String? = null,

	@field:JsonProperty("qr_data")
	val qrData: String? = null,

	@field:JsonProperty("position")
	val position: String? = null,

	@field:JsonProperty("adminfee")
	val adminfee: Long? = null,

	@field:JsonProperty("subamount")
	val subamount: Long? = null,

	@field:JsonProperty("items")
	val items: List<BogasariInquiryProduct?>? = null
)