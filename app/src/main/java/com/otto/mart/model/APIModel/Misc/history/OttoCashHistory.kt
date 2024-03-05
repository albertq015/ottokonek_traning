package com.otto.mart.model.APIModel.Misc.history

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.SerializedName

@JsonIgnoreProperties(ignoreUnknown = true)
class OttoCashHistory {
    var transactionType: String? = null
    var amount: Long? = null
    var referenceNumber: String? = null
    var description: String? = null
    var id: String? = null
    var valueDate: String? = null
    var status: String? = null
}