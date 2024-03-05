package com.otto.mart.model.APIModel.Request

data class PostAcquisitionRequest (
        var id_card_image: String? = null,
        val phone: String? = null,
        var merchant_image: String? = null,
        val id_card_type: String? = null,
        val latitude: Double? = null,
        val longitude: Double? = null,
        val id_card_number: String? = null,
        var business_location: String? = null,
        var business_category: String? = null,
        var business_type: String? = null,
        var operational_hour: String? = null,
        var visiting_hour: String? = null,
        var region: String? = null,
        var province: String? = null,
        var municipality: String? = null,
        var barangay: String? = null
)