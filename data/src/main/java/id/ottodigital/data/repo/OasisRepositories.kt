package id.ottodigital.data.repo

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import id.ottodigital.data.helper.AuthKeyModule
import id.ottodigital.data.remote.oasis.OASISService

@SuppressLint("CheckResult")
class OasisRepositories(val oasisService : OASISService ) : BaseRepo(){
    override fun getAuthKey(): String = AuthKeyModule.IDM_AUTH_VALUE
    var instance : OasisRepositories? = null
    //var listData : MutableLiveData<com.otto.mart.model.APIModel.Response.grosir.HistoryOasisOrderResponseModel>? = null

    /*fun getinstance(): OasisRepositories? {
        return instance
    }

    fun getHistoryList(request : com.otto.mart.model.APIModel.Request.grosir.HistoryOasisOrderRequestModel){
        getHistoryListFromServer(request)
    }

    fun getHistoryListFromServer (request : com.otto.mart.model.APIModel.Request.grosir.HistoryOasisOrderRequestModel) =
            oasisService.serviceGetHistoryOasisOrder(auth, request)*/

}