package com.otto.mart.model.localmodel.ottopoin

import com.google.gson.Gson

class OttoPoinLogin(
        val phone: String,
        val expireTime: String,
        val institutionId: String,
        val channelId: String,
        val appsId: String,
        val deviceId: String,
        var geolocation: String
) {
    fun getJson(): String = Gson().toJson(this)
}