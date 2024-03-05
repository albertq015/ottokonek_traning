package id.ottodigital.data.remote.idm

import id.ottodigital.data.entity.idm.request.CheckAccountRequestModel
import id.ottodigital.data.entity.idm.response.CheckAccountResponseModel
import id.ottodigital.data.entity.idm.response.OrderHistoriesResponseModel
import id.ottodigital.data.entity.idm.response.TokenResponseModel
import io.reactivex.Observable
import retrofit2.http.*

interface IDMService {
    @GET("orders/histories")
    fun serviceOrderHistories(
            @HeaderMap header: Map<String, String>,
            @Query("status") status: String,
            @QueryMap params: Map<String, String>
    ): Observable<OrderHistoriesResponseModel>

    @POST("auth/token/request")
    fun serviceGetToken(
            @Body model: CheckAccountRequestModel
    ): Observable<TokenResponseModel>

    @GET("users/{userId}")
    fun serviceGetAccount(
            @HeaderMap header: Map<String, String>,
            @Path("userId") model: String?
    ): Observable<CheckAccountResponseModel>
}