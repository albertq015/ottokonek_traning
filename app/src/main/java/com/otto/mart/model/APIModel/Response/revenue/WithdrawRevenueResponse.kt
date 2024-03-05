package com.otto.mart.model.APIModel.Response.revenue

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel

@JsonIgnoreProperties(ignoreUnknown = true)
class WithdrawRevenueResponse : BaseResponseModel() {
    var data: Data? = null

    class Data {
        var key_value_list: List<WidgetBuilderModel>? = null
    }
}