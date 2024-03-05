package id.ottodigital.core

import android.content.Context
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideHttpLoggingIntercept(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BASIC) }
    }

    @Provides
    @Singleton
    fun provideCache(context: Context): Cache {
        return Cache(context.cacheDir, 10.times(1024).times(1024).toLong())
    }

    @Provides
    @Singleton
    fun provideHttpClientBuilder(context: Context, cache: Cache, httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient.Builder {
//        val builder = OkHttpClient.Builder()
//                .cache(cache)
//                .addInterceptor(ChuckerInterceptor(context))

        val builder = OkHttpClient.Builder()
                .cache(cache)

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(httpLoggingInterceptor)
        }

        return builder
    }

    @Provides
    @Singleton
    fun provideRetrofitBuilder() =
            Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

}