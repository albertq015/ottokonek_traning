package id.ottodigital.core

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.ActivityCompat
import dagger.Module
import dagger.Provides
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

@Module
class DeviceModule {

    companion object {
        const val DEVICE_ID = "getImeiData"
    }

    @Provides
    @Singleton
    @Named(DEVICE_ID)
    fun provideImeiCode(context: Context): String {
        val manager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//        return if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
//            if (manager.deviceId != null) manager.deviceId
//            else DeviceId.getDeviceID(context)
//        } else UUID.randomUUID().toString()
        return  UUID.randomUUID().toString()

    }
//        val manager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//        return if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
//            if (manager.deviceId != null) manager.deviceId
//            else DeviceId.getDeviceID(context)
//        } else UUID.randomUUID().toString()
//    }
}