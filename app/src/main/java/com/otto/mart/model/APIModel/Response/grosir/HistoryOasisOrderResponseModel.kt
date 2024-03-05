package com.otto.mart.model.APIModel.Response.grosir

import app.beelabs.com.codebase.base.response.BaseResponse
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class HistoryOasisOrderResponseModel(var data: HistoryOasisOrderResponseData? = null) : BaseResponse()








