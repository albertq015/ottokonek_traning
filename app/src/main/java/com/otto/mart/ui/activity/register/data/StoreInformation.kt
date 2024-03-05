package com.otto.mart.ui.activity.register.data

data class StoreInformation (
        var name: String? = null,
        var code: String? = null,
        var category: MutableList<StoreCategory>? = null
)