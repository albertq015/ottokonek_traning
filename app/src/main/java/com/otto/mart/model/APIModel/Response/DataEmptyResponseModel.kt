package com.otto.mart.model.APIModel.Response

import com.fasterxml.jackson.annotation.JsonProperty
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel

class DataEmptyResponseModel(
        @JsonProperty("data")
        val data: Any? = null
):BaseResponseModel()