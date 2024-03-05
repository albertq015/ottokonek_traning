package com.otto.mart.model.APIModel.Request.grosir

import com.fasterxml.jackson.annotation.JsonProperty

class GrosirPostingRequestV2 : GrosirPostingRequest() {

    var payment: PayAccountDetail? = null
}