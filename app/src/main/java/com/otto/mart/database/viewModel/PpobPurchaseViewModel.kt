package com.otto.mart.database.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.otto.mart.database.entities.PpobPurchase
import com.otto.mart.database.repository.PpobPurchaseRepository


class PpobPurchaseViewModel(application: Application) : AndroidViewModel(application) {

    private val mRepository: PpobPurchaseRepository

    init {
        mRepository = PpobPurchaseRepository(application)
    }

    fun insert(ppobPurchase: PpobPurchase) {
        mRepository.insert(ppobPurchase)
    }

    fun getProduct(productName: String, productCode: String) : LiveData<List<PpobPurchase>> {
        return mRepository.getProduct(productName, productCode)
    }

    fun getProductByName(productName: String) : LiveData<List<PpobPurchase>> {
        return mRepository.getProductByName(productName)
    }
}