package com.otto.mart.model.APIModel.Response.grosir

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class HistoryOasisOrderShipment : Serializable{
    var recipient_name: String? = null
    var recipient_phone: String? = null
    var recipient_email: String? = null
    var recipient_province_name: String? = null
    var recipient_city_name: String? = null
    var recipient_district_name: String? = null
    var recipient_address_name: String? = null
    var recipient_address_detail: String? = null
    var recipient_zip_code: Long? = null
    var courier_name: String? = null
    var courier_service: String? = null
    var courier_description: String? = null
    var courier_cost: Double? = null
    var weight: Long? = null
}