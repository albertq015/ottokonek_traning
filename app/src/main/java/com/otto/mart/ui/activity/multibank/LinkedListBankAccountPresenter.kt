package com.otto.mart.ui.activity.multibank

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import com.otto.mart.OttoMartApp
import com.otto.mart.api.OttoKonekAPI
import com.otto.mart.model.APIModel.Request.multibank.IssuerLinkedBalance
import com.otto.mart.model.APIModel.Response.multibank.AccountListResponse
import com.otto.mart.model.APIModel.Response.multibank.IssuerLinkedBalanceResponse
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.ApiCallback
import com.otto.mart.support.util.ApiRoseCallback

interface ListLinkedBankAccountPresenter {
    fun setDataAccount(data: MutableList<AccountListResponse.Data>)
    fun getDataAccount(): MutableLiveData<MutableList<LLBAViewModel>>
    fun onRequestRetry(pos: Int)
    fun loadBanklistAPI()
    fun loadIssuerBalance()
    fun checkData(): Boolean
}

class ListLinkedBankAccountPresenterImpl(mContext: Context) : ListLinkedBankAccountPresenter {
    companion object {
        private var dataset = MutableLiveData<MutableList<LLBAViewModel>>()
    }

    override fun setDataAccount(data: MutableList<AccountListResponse.Data>) {
        val builder: MutableList<LLBAViewModel> = ArrayList()
        for (datum in data)
            builder.add(LLBAViewModel().apply { object1 = datum })
        dataset.value = builder
    }

    override fun getDataAccount(): MutableLiveData<MutableList<LLBAViewModel>> {
        return dataset
    }

    override fun onRequestRetry(pos: Int) {
        trycount = 3
        dataset.value?.get(pos)?.object1?.accountNumber?.let {
            loadIssuerBalAPI(
                pos,
                it, dataset.value!![pos].object1!!.bin,
                        dataset.value!![pos].object1!!.id

            )
        }
    }


    override fun loadIssuerBalance() {
        if (dataset.value != null)
            for ((order, vm) in dataset.value!!.withIndex())
                loadIssuerBalAPI(order, vm.object1?.accountNumber ?: "", vm.object1?.bin ?: "",vm.object1?.id ?: 0)
    }

    override fun checkData(): Boolean {
        return dataset.value?.isNotEmpty() ?: false
    }

    private fun loadIssuerBalAPI(ordering: Int, an: String, bn: String,idList:Int) {
        val requestBody = IssuerLinkedBalance().apply {
            accountNumber = an
            bin = bn
            id = idList
        }

        OttoKonekAPI.issuerLinkedBalance(
            OttoMartApp.getContext(), requestBody,
            object : ApiCallback<IssuerLinkedBalanceResponse>() {
                override fun onResponseSuccess(body: IssuerLinkedBalanceResponse?) {



                    when (body?.baseMeta?.code) {
                        in 200..299 -> {
                            dataset.postValue(dataset.value?.also {
                                it[ordering].object2 = body?.data
                            })
                        }
                        in 400..497 -> {
                            if (!retryBalReq(ordering, an, bn,idList)) {
                                //testcode
                                dataset.postValue(dataset.value?.also {
                                    it[ordering].object2 =
                                        IssuerLinkedBalanceResponse.ILBRData().apply {
                                            availableBalance = 0.0
                                            errorval = body?.baseMeta?.message ?: "error"
                                        }
                                })

                            }
                        }
                        in 498..500 ->{
                            if (!retryBalReq(ordering, an, bn,idList)) {
                                //testcode
                                dataset.postValue(dataset.value?.also {
                                    it[ordering].object2 =
                                        IssuerLinkedBalanceResponse.ILBRData().apply {
                                            availableBalance = 0.0
                                            errorval = body?.baseMeta?.message ?: "error"
                                            errorCodeResponse = body?.baseMeta?.code
                                        }
                                })

                            }
                        }
                        else -> {
                            if (!retryBalReq(ordering, an, bn,idList)) {
                                //testcode
                                dataset.postValue(dataset.value?.also {
                                    it[ordering].object2 =
                                        IssuerLinkedBalanceResponse.ILBRData().apply {
                                            availableBalance = 0.0
                                            errorval = body?.baseMeta?.message ?: "error"
                                        }
                                })

                            }
                        }
                    }


                }

                override fun onApiServiceFailed(throwable: Throwable?) {
                    dataset.postValue(dataset.value?.also {
                        it[ordering].object2 =
                            IssuerLinkedBalanceResponse.ILBRData().apply {
                                availableBalance = 0.0
                                errorval = "API error"
                            }
                    })
                }

            }
        )
    }

    override fun loadBanklistAPI() {

        OttoKonekAPI.getListAccountBank(
            OttoMartApp.getContext(),

            object :
                ApiRoseCallback<AccountListResponse>() {
                override fun onResponseSuccess(body: AccountListResponse?) {
                    if (body != null) {
                        val a: MutableList<LLBAViewModel> = ArrayList()
                        for (data in body.data) {
                            a.add(LLBAViewModel().apply { object1 = data })
//                            a.add(LLBAViewModel().apply { object1 = data }) test
                        }
                        dataset.postValue(a)
                        loadIssuerBalance()
                    }

                }

                override fun onApiServiceFailed(throwable: Throwable?) {

                }

            })
    }


    private var trycount = 0
    private fun retryBalReq(ordering: Int, an: String, bn: String,idList: Int): Boolean {
        return if (trycount <= 2) {
            trycount++
            Handler(Looper.getMainLooper()).postDelayed({
                loadIssuerBalAPI(ordering, an, bn,idList)
            }, 3000)
            true
        } else
            false
    }

}

class LLBAViewModel {
    var object1: AccountListResponse.Data? = null
    var object2: IssuerLinkedBalanceResponse.ILBRData? = null
}

interface apiOutput {
    fun output(body: AccountListResponse)
}




