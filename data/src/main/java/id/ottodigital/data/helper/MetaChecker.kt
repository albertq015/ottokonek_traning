package id.ottodigital.data.helper

import androidx.lifecycle.MutableLiveData
import id.ottodigital.data.entity.base.BaseResponse
import id.ottodigital.data.entity.base.Meta

object MetaChecker {
    @JvmStatic
    val metaLiveData = MutableLiveData<Meta>()

    fun sessionCheck(meta: Meta) {
        println("log for meta code ${meta.code}")
        if (meta.code == 486) {
            metaLiveData.postValue(meta)
        }
    }
}

fun <T> MutableLiveData<T>.postValueWithMetaCheck(value: T) {
    if (value is BaseResponse) {
        value.meta?.let { MetaChecker.sessionCheck(value.meta) }
    }
    postValue(value)
}