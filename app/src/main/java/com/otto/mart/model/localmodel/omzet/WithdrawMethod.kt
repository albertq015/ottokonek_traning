package com.otto.mart.model.localmodel.omzet

data class WithdrawMethod (
        var method: METHOD,
        var name: String,
        var desc: String,
        var minLimit: Long,
        var maxLimit: Long,
        var adminFee: Int,
        var isSelected: Boolean,
        var isEnable: Boolean
) {
    enum class METHOD {
        VA,
        INSTANT,
        SKN,
        DEPOSIT_INSTANT,
        DEPOSIT_SKN
    }
}