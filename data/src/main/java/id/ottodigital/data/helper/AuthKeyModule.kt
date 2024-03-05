package id.ottodigital.data.helper

import dagger.Module
import dagger.Provides

@Module
class AuthKeyModule {

    companion object {
        const val NAMED_AUTH_KEY = "authKey"
        const val IDM_AUTH_VALUE = "idmAuthKey"
        const val OP_AUTH_VALUE = "opAuthKey"
    }

    @Provides
    fun provideIdmAuthKey() = IDM_AUTH_VALUE
}