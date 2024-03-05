package com.otto.mart.model.APIModel.Response.grosir

import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel

class CheckEligibleResponse : BaseResponseModel() {
    var data: Data? = null

    class Data {
        var isRegistered: Boolean? = null
        var isCan_order: Boolean? = null
        var isEligible: Boolean? = null

    }
}