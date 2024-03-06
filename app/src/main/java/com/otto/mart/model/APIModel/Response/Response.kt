package com.otto.mart.model.APIModel.Response

import com.google.gson.annotations.SerializedName

class Response {
    @SerializedName("data")
    var data :Data? = null
    @SerializedName("meta")
    var meta :Meta? = null
}

class Data {
    @SerializedName("version_app")
    var version = ""
    @SerializedName("version_api")
    var api = ""
    @SerializedName("force_update")
    var update:Boolean = false
    @SerializedName("app_id")
    var id = ""
}

class Meta{
    @SerializedName("status")
    var status:Boolean = false
    @SerializedName("code")
    var code = 0
    @SerializedName("message")
    var message:String = ""
}
