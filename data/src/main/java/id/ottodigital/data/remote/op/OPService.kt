package id.ottodigital.data.remote.op

import com.google.gson.JsonElement
import id.ottodigital.data.model.register.request.SearchMerchantRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface OPService {

    @POST("auth/v1.0/ottokonek/signup/searchmerchant")
    fun searchMerchantService(@Body body: SearchMerchantRequest): Call<JsonElement>
}