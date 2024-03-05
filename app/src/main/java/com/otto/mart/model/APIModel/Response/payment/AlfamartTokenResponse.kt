package com.otto.mart.model.APIModel.Response.payment

import app.beelabs.com.codebase.base.response.BaseResponse
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel

class AlfamartTokenResponse: BaseResponseModel() {
    var data: Data? = null

    class Data {
        var token: String? = ""
        var ref: String? = ""
        var msisdn: String? = ""
    }
}