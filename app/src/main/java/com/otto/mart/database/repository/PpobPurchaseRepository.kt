package com.otto.mart.database.repository

import android.app.Application
import androidx.lifecycle.LiveData
import android.os.AsyncTask
import com.otto.mart.database.AppDatabase
import com.otto.mart.database.dao.PpobPurchaseDao
import com.otto.mart.database.entities.PpobPurchase

internal class PpobPurchaseRepository

    (application: Application) {

    private val mPpobPurchaseDao: PpobPurchaseDao

    init {
        val db = AppDatabase.getDatabase(application)
        mPpobPurchaseDao = db!!.ppobPurchaseDao()
    }

    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    fun insert(ppobPurchase: PpobPurchase) {
        insertAsyncTask(mPpobPurchaseDao).execute(ppobPurchase)
    }

    private class insertAsyncTask internal constructor(private val mAsyncTaskDao: PpobPurchaseDao) :
        AsyncTask<PpobPurchase, Void, Void>() {

        override fun doInBackground(vararg params: PpobPurchase): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }

    fun getProduct(productName: String, productCode: String) : LiveData<List<PpobPurchase>> {
        return mPpobPurchaseDao.getProduct(productName, productCode)
    }

    fun getProductByName(productName: String) : LiveData<List<PpobPurchase>> {
        return mPpobPurchaseDao.getProductByName(productName)
    }
}