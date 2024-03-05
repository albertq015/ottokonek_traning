package id.ottodigital.data.di.provider

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import java.lang.IllegalStateException

fun Activity.dataComponent() =
        (applicationContext as? DataComponentProvider)?.provideDataComponent()
                ?: throw IllegalStateException("DataComponentProvider not implemented: $applicationContext")