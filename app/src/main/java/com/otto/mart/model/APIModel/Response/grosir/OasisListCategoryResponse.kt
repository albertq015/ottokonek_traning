package com.otto.mart.model.APIModel.Response.grosir

import app.beelabs.com.codebase.base.response.BaseResponse

data class OasisListCategoryResponse (var data : List<OasisItemCategoryResponse>? = null) : BaseResponse()