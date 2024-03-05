package com.otto.mart.model.APIModel.Response.bogasari

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class BogasariInquiryResponse(

        @JsonProperty("data")
        var data: BogasariInquiryResponse? = null
)