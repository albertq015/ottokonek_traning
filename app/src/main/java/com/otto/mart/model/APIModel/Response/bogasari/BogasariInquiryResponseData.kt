package com.otto.mart.model.APIModel.Response.bogasari

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class BogasariInquiryResponseData(

        @field:JsonProperty("inquiry_data")
        val inquiryData: BogasariInquiryData? = null
)