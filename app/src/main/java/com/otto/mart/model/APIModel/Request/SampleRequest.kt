package com.otto.mart.model.APIModel.Request

import com.google.gson.annotations.SerializedName

class SampleRequest {
    @SerializedName("versionApp")
    var id = 0
    @SerializedName("application_id")
    var app_id:String = ""
    @SerializedName("version_app")
    var version = 0
}