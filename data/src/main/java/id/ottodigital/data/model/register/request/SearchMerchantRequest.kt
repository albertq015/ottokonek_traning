package id.ottodigital.data.model.register.request

import com.google.gson.annotations.SerializedName

data class SearchMerchantRequest(
        @SerializedName("phone_number")
        val phoneNumber: String
)