package com.otto.mart.model.APIModel.Request.bogasari

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.Parcelize

@JsonIgnoreProperties(ignoreUnknown = true)
@Parcelize
data class BogasariInquiryProduct(

        @field:JsonProperty("item_unit_price")
        val itemUnitPrice: Long? = null,

        @field:JsonProperty("item_name")
        val itemName: String? = null,

        @field:JsonProperty("sku")
        val sku: String? = null,

        @field:JsonProperty("item_quantity")
        val itemQuantity: Int? = null
) : Parcelable