package com.otto.mart.di

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import id.ottodigital.core.Event
import id.ottodigital.core.observableIOMain
import id.ottodigital.data.helper.toLiveData
import id.ottodigital.data.entity.idm.request.CheckAccountRequestModel
import id.ottodigital.data.entity.idm.response.CheckAccountResponseModel
import id.ottodigital.data.entity.idm.response.TokenResponseModel
import id.ottodigital.data.repo.LoginRepo
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class VMLogin @Inject constructor(
        val compositeDisposable: CompositeDisposable,
        val repo: LoginRepo) : ViewModel() {

    private val tokenLiveData by lazy { MutableLiveData<Event<TokenResponseModel>>() }
    val accountLiveData = Transformations.switchMap(tokenLiveData) { it ->
        val accountLiveData = MutableLiveData<Event<CheckAccountResponseModel>>()
        when (it) {
            is Event.Success -> it.result.data?.user_id?.let { userId -> getIdmAccount(userId, accountLiveData) }
            is Event.Failure -> it.message.let { errorMessage -> accountLiveData.postValue(Event.failure(errorMessage)) }
            else -> accountLiveData.postValue(Event.loading((it as Event.Loading).loading))
        }
        accountLiveData
    }

    fun doLogin(phone: String) {
        repo.doLogin(getAccountRequest(phone))
                .observableIOMain()
                .doOnSubscribe { tokenLiveData.postValue(Event.loading(true)) }
                .toLiveData(tokenLiveData, compositeDisposable)
    }

    private fun getIdmAccount(userId: Long, accountLiveData: MutableLiveData<Event<CheckAccountResponseModel>>) {
        repo.getAccount(userId)
                .observableIOMain()
                .toLiveData(accountLiveData, compositeDisposable)
    }

    private fun getAccountRequest(phone: String): CheckAccountRequestModel =
            CheckAccountRequestModel().apply {
                this.phone = phone
            }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}