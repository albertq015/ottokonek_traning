package com.otto.mart.model.localmodel.ottopoin

data class OttoPoinLoginResponse(
        val accountStatus: String,
        val institutionAccType: String,
        val msg: String,
        val token: String
)