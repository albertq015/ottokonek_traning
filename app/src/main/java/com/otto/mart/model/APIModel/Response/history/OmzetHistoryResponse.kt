package com.otto.mart.model.APIModel.Response.history

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.otto.mart.model.APIModel.Misc.history.OmzetHistory
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel

@JsonIgnoreProperties(ignoreUnknown = true)
class OmzetHistoryResponse : BaseResponseModel() {
    var data: List<OmzetHistory>? = null
    var count = 0
}