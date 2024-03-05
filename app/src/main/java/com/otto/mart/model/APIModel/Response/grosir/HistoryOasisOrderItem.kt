package com.otto.mart.model.APIModel.Response.grosir


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable


@JsonIgnoreProperties(ignoreUnknown = true)

class HistoryOasisOrderItem : Serializable {
    var order_no: String? = null
    var order_date: String? = null
    var total_amount: Double? = null
    var status_order: String? = null
    var payment_status: String? = null
    var product: ArrayList<HistoryOasisOrderProduct>? = null
    var supplier_id: Long? = null
    var supplier_name: String? = null
    var payment_method: PaymentMethodOasisList?  = null
    var payment_reference: String? = null
    var courier_cost: Double? = null
    var reff_no : String? = null
    var shipment: HistoryOasisOrderShipment? = null
    var image_path: String? = null
    var payment_type : PaymentTypeResponse? = null
    var source_bank : String? = null

}