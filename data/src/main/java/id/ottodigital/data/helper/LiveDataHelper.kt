package id.ottodigital.data.helper

import androidx.lifecycle.MutableLiveData
import id.ottodigital.core.Event
import id.ottodigital.core.getErrorPaymentMessage
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

fun <T> Observable<T>.toLiveData(data: MutableLiveData<Event<T>>? = null, compositeDisposable: CompositeDisposable): MutableLiveData<Event<T>> {
    val tempVal = data ?: MutableLiveData()
    compositeDisposable.add(
            this.subscribe({
                tempVal.postValueWithMetaCheck(Event.loading(false))
                tempVal.postValue(Event.success(it))
            }, {
                tempVal.postValue(Event.loading(false))
                tempVal.postValue(Event.failure(it.getErrorPaymentMessage()))
            })
    )

    return tempVal
}