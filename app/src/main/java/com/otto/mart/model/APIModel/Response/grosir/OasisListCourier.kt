package com.otto.mart.model.APIModel.Response.grosir

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class OasisListCourier(
    var code: String? = null,
    var id: Int? = null,
    var name: String? = null,
    var price: Double? = null,
    var etd: String? = null,
    var image_path: String? = null
)