package com.otto.mart.di.module

import androidx.lifecycle.ViewModel
import com.otto.mart.ui.activity.multibank.ListLinkedBankAccountPresenter
import com.otto.mart.ui.activity.multibank.ListLinkedBankAccountPresenterImpl
import org.koin.dsl.module

    val LLBAPresenter = module {
        single { ListLinkedBankAccountPresenterImpl(get()) as ListLinkedBankAccountPresenter }

}