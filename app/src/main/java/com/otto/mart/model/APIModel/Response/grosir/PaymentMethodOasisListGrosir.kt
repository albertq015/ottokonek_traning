package com.otto.mart.model.APIModel.Response.grosir

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable


@JsonIgnoreProperties(ignoreUnknown = true)

class PaymentMethodOasisListGrosir : Serializable {
    var code: String? = null
    var name: String? = null
    var description: String? = null
    var payment_type: ArrayList<PaymentTypeResponse>? = null
}
