package com.otto.mart.model.APIModel.Response.bogasari

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class BogasariProductResponse(

        @JsonProperty("bogasari")
        var bogasari: List<BogasariProduct>? = null
)