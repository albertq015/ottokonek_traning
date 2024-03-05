package id.ottodigital.data.di.module

import dagger.Module
import dagger.Provides
import id.ottodigital.core.ServiceHelper
import id.ottodigital.data.BuildConfig
import id.ottodigital.data.di.scope.DataScope
import id.ottodigital.data.remote.idm.IDMService
import id.ottodigital.data.remote.op.OPService

@Module
class ServiceModule {

    @DataScope
    @Provides
    fun provideIDMService(serviceHelper: ServiceHelper): IDMService =
            serviceHelper.provideService(url = BuildConfig.BASEURLINDOMARCO, serviceClass = IDMService::class.java)

    @DataScope
    @Provides
    fun provideOPService(serviceHelper: ServiceHelper): OPService =
            serviceHelper.provideService(url = BuildConfig.OTTOKONEK_SERVER_URL, serviceClass = OPService::class.java)
}