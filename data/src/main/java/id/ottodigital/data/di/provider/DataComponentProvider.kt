package id.ottodigital.data.di.provider

import id.ottodigital.data.di.component.DataComponent

interface DataComponentProvider {
    fun provideDataComponent(): DataComponent
}