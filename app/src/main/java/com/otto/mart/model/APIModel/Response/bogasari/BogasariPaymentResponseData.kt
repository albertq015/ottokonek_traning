package com.otto.mart.model.APIModel.Response.bogasari

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel

@JsonIgnoreProperties(ignoreUnknown = true)
data class BogasariPaymentResponseData(

        @JsonProperty("key_value_list")
        var keyValueList: List<WidgetBuilderModel>? = null

): BaseResponseModel()