package com.otto.mart.model.APIModel.Response.transfer

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel

@JsonIgnoreProperties(ignoreUnknown = true)
class TransferLimitResponse : BaseResponseModel() {
    var data: Data? = null

    @JsonIgnoreProperties(ignoreUnknown = true)
    class Data {
        var bank: Limit? = null
        var ottocash: Limit? = null
        var deposit: Limit? = null

        @JsonIgnoreProperties(ignoreUnknown = true)
        class Limit {
            var min_limit: String = ""
            var max_limit: String = ""
        }
    }
}