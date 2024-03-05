package com.otto.mart.model.APIModel.Response.grosir

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class OasisListMethodPickUp (
    var code : String? = null,
    var name : String? = null,
    var national_holiday :String?=null,
    var operational_day : String? = null,
    var operational_hour : String? = null
)