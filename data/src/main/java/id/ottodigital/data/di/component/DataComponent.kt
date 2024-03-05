package id.ottodigital.data.di.component

import dagger.Component
import id.ottodigital.core.CoreComponent
import id.ottodigital.data.di.module.PreferenceModule
import id.ottodigital.data.di.module.ServiceModule
import id.ottodigital.data.di.scope.DataScope
import id.ottodigital.data.helper.AuthHeader
import id.ottodigital.data.manager.IDMSessionManager
import id.ottodigital.data.manager.OPSessionManager
import id.ottodigital.data.pref.PrefHelper
import id.ottodigital.data.repo.LoginRepo
import id.ottodigital.data.repo.OrderStatusRepo

@DataScope
@Component(dependencies = [CoreComponent::class],
        modules = [
            PreferenceModule::class,
            ServiceModule::class
        ])

interface DataComponent {
    fun getPref(): PrefHelper
    fun authHeader(): AuthHeader
    fun orderStatusRepo(): OrderStatusRepo
    fun idmLoginRepo(): LoginRepo
    fun idmSessionManager():IDMSessionManager
    fun opSessionManager():OPSessionManager
}