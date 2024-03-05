package com.otto.mart.model.APIModel.Request.grosir

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class PayAccountDetail : Serializable {
    @JsonProperty("account_number")
    var accountNumber = ""
    var bin = ""
    @JsonProperty("name_bin")
    var nameBin = ""

}