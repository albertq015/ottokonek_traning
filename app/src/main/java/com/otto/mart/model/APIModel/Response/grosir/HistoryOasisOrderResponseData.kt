package com.otto.mart.model.APIModel.Response.grosir

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class HistoryOasisOrderResponseData(
        var on_progress: List<com.otto.mart.model.APIModel.Response.grosir.HistoryOasisOrderItem>? = null,
        var done: List<com.otto.mart.model.APIModel.Response.grosir.HistoryOasisOrderItem>? = null,
        var cancelled: List<com.otto.mart.model.APIModel.Response.grosir.HistoryOasisOrderItem>? = null
)