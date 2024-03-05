package com.otto.mart.model.APIModel.Response.multibank

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


@JsonIgnoreProperties(ignoreUnknown = true)
data class IssuerLinkedListItem(
    var id: Int? = null,
    var mid: String? = null,
    var accountName: String? = null,
    var accountType: String? = null,
    var accountNumber: String? = null,
    var accountStatus: String? = null,
    var status: String? = null,
    var bin: String? = null,
    var binName: String? = null,
    var cid: String? = null,
    var logo: String? = null
)

