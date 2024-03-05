package com.otto.mart.model.localmodel.ottopoin

import com.google.gson.Gson

class OttoPoinRegister(
        val firstname: String,
        val lastname: String,
        val phone: String,
        val email: String,
        val gender: String,
        val birthdate: String,
        var expireTime: String
) {
    fun getJson(): String = Gson().toJson(this)
}