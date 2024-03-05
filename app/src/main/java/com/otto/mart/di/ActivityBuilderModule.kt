package com.otto.mart.di

import com.otto.mart.ui.activity.login.LoginActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeLogin():LoginActivity
}