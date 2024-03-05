package id.ottopay.oasis.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.otto.mart.model.APIModel.Response.grosir.HistoryOasisOrderResponseModel
import id.ottodigital.core.Event
import id.ottodigital.data.repo.OasisRepositories
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class VMOasis @Inject constructor(
        val compositeDisposable: CompositeDisposable,
        val repo : OasisRepositories) : ViewModel(){
        val historyListData = MutableLiveData<Event<HistoryOasisOrderResponseModel>>()


//    fun getListHistory(request : HistoryOasisOrderRequestModel){
//        repo.getHistoryListFromServer(request).observableIOMain()
//                .toLiveData(historyListData,compositeDisposable)
//    }
    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}