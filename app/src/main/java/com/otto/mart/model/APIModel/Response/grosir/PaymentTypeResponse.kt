package com.otto.mart.model.APIModel.Response.grosir


import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class PaymentTypeResponse : Serializable{
    var code: String? = null
    var name: String? = null
}


