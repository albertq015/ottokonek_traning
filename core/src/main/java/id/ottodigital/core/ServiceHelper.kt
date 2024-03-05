package id.ottodigital.core

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Inject

open class ServiceHelper @Inject constructor() {

    @set:Inject
    lateinit var retrofitBuilder: Retrofit.Builder

    @set:Inject
    lateinit var httpClientBuilder: OkHttpClient.Builder

    fun <T> provideService(url: String, timeoutInSecond: Long = 30, serviceClass: Class<T>): T {
        val client = httpClientBuilder
                .connectTimeout(timeoutInSecond, TimeUnit.SECONDS)
                .readTimeout(timeoutInSecond, TimeUnit.SECONDS)
                .writeTimeout(timeoutInSecond, TimeUnit.SECONDS)
                .build()

        return retrofitBuilder.baseUrl(url)
                .client(client)
                .build()
                .create(serviceClass)
    }
}