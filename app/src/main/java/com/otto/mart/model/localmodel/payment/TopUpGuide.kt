package com.otto.mart.model.localmodel.payment

data class TopUpGuide (
    var title: String = "",
    var stepList: MutableList<String> = mutableListOf(),
    var isShow: Boolean = false
)