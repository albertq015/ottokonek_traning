package com.otto.mart.model.localmodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WebViewContent (
    var title: String = "",
    var url: String = ""
) : Parcelable