package com.otto.mart.model.APIModel.Response.grosir

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class HistoryOasisOrderProductDetail : Serializable {
    var id: Long? = null
    var order_id: Long? = null
    var product_code: String? = null
    var photo: String? = null
    var name: String? = null
    var sell_price: Double? = null
    var quantity: Long? = null
    var weight: Long? = null
    var total_amount: Float? = null
}