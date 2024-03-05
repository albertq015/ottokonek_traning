package com.otto.mart.di

import com.otto.mart.OttoMartApp
import dagger.Component
import dagger.android.AndroidInjectionModule
import id.ottodigital.core.CoreComponent
import id.ottodigital.data.di.component.DataComponent

@AppScope
@Component(dependencies = [DataComponent::class,CoreComponent::class], modules = [AndroidInjectionModule::class, ActivityBuilderModule::class, ViewModelModule::class])
interface ActivityComponent {

    fun inject(app: OttoMartApp)
}