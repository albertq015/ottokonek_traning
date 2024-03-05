package com.otto.mart.model.APIModel.Response.grosir

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class HistoryOasisOrderProduct : Serializable{
    var total_amount: Float? = null
    var quantity: Int? = null
    var product_details: HistoryOasisOrderProductDetail? = null
}