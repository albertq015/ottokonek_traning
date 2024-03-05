package com.otto.mart.model.APIModel.Request.bogasari

import com.fasterxml.jackson.annotation.JsonProperty

data class BogasariPaymentRequestModel(

	@field:JsonProperty("product_code")
	val productCode: String? = null,

	@field:JsonProperty("walletId")
	val walletId: Int? = null,

	@field:JsonProperty("amount")
	val amount: Long? = null,

	@field:JsonProperty("sub_amount")
	val subAmount: Long? = null,

	@field:JsonProperty("admin_fee")
	val adminFee: Long? = null,

	@field:JsonProperty("pin")
	val pin: String? = null,

	@field:JsonProperty("rrn")
	val rrn: String? = null
)