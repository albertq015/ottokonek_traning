package com.otto.mart.model.APIModel.Response.register

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class SearchMerchantData(
        @JsonProperty("idType") var idType: String? = null,
        @JsonProperty("mpan") var mpan: String? = null,
        @JsonProperty("visitingHour") var visitingHour: String? = null,
        @JsonProperty("bin") var bin: String? = null,
        @JsonProperty("businessLocation") var businessLocation: String? = null,
        @JsonProperty("accountType") var accountType: String? = null,
        @JsonProperty("municipality") var municipality: String? = null,
        @JsonProperty("mid") var mid: String? = null,
        @JsonProperty("accountNumber") var accountNumber: String? = null,
        @JsonProperty("idNumber") var idNumber: String? = null,
        @JsonProperty("merchantName") var merchantName: String? = null,
        @JsonProperty("phoneNumber") var phoneNumber: String? = null,
        @JsonProperty("ownerName") var ownerName: String? = null,
        @JsonProperty("province") var province: String? = null,
        @JsonProperty("operationalHour") var operationalHour: String? = null,
        @JsonProperty("dob") var dob: String? = null,
        @JsonProperty("businessCategory") var businessCategory: String? = null,
        @JsonProperty("businessCategoryName") var businessCategoryName: String? = null,
        @JsonProperty("merchantAddress") var merchantAddress: String? = null,
        @JsonProperty("businessLocationType") var businessLocationType: String? = null,
        @JsonProperty("businessType") var businessType: String? = null,
        @JsonProperty("region") var region: String? = null,
        @JsonProperty("barangay") var barangay: String? = null,
        @JsonProperty("cid") var cid: String? = null,
        @JsonProperty("status") var status: String? = null
)

