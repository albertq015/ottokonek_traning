package com.otto.mart.api

import com.otto.mart.model.APIModel.Request.SampleRequest
import com.otto.mart.model.APIModel.Response.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import javax.security.auth.callback.Callback

interface Client {

    @POST("/ottopay/v0.1.0/ottopay-mart/check-version")
    fun dataAPI(@Body sampleRequest: SampleRequest): Call<Response>


}