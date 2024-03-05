package com.otto.mart.model.APIModel.Response.bogasari

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.Parcelize

@JsonIgnoreProperties(ignoreUnknown = true)
@Parcelize
data class BogasariProduct(

        @field:JsonProperty("name")
        var name: String? = null,

        @field:JsonProperty("price")
        var price: Long? = null,

        @field:JsonProperty("qty")
        var quantity: Int? = null,

        @field:JsonProperty("sku")
        var sku: String? = null,

        @field:JsonProperty("image_url")
        var imageUrl: String? = null
) : Parcelable