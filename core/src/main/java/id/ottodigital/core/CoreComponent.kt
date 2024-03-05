package id.ottodigital.core

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import io.reactivex.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class, NetworkModule::class,DeviceModule::class])
interface CoreComponent {
    fun serviceHelper():ServiceHelper
    fun context(): Context
    fun compositeDisposable(): CompositeDisposable

    @Named(DeviceModule.DEVICE_ID)
    fun deviceId(): String
}