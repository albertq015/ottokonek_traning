package id.ottodigital.data.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import id.ottodigital.data.di.scope.DataScope
import id.ottodigital.data.pref.PrefHelper

@Module
class PreferenceModule {

    @DataScope
    @Provides
    fun providePreference(context: Context) =
            PrefHelper(context)
}