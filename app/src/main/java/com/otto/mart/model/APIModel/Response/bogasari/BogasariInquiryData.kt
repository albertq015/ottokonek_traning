package com.otto.mart.model.APIModel.Response.bogasari

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonIgnoreProperties(ignoreUnknown = true)
data class BogasariInquiryData(

        @field:JsonProperty("product_code")
        val productCode: String? = null,

        @field:JsonProperty("amount")
        val amount: Long? = null,

        @field:JsonProperty("admin_fee")
        val adminFee: Long? = null,

        @field:JsonProperty("rrn")
        val rrn: String? = null

) : Parcelable