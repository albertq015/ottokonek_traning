package com.otto.mart.database.repository

import android.app.Application
import androidx.lifecycle.LiveData
import android.os.AsyncTask
import com.otto.mart.database.AppDatabase
import com.otto.mart.database.dao.UserDao
import com.otto.mart.database.entities.User

internal class UserRepository

    (application: Application) {

    private val mUserDao: UserDao
    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allUsers: LiveData<List<User>>

    init {
        val db = AppDatabase.getDatabase(application)
        mUserDao = db!!.userDao()
        allUsers = mUserDao.getAllUsers()
    }

    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    fun insert(user: User) {
        insertAsyncTask(mUserDao).execute(user)
    }

    private class insertAsyncTask internal constructor(private val mAsyncTaskDao: UserDao) :
        AsyncTask<User, Void, Void>() {

        override fun doInBackground(vararg params: User): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }

    fun update(user: User) {
        updateAsyncTask(mUserDao).execute(user)
    }

    private class updateAsyncTask internal constructor(private val mAsyncTaskDao: UserDao) :
        AsyncTask<User, Void, Void>() {

        override fun doInBackground(vararg params: User): Void? {
            mAsyncTaskDao.update(params[0])
            return null
        }
    }


    fun delete(user: User) {
        deleteAsyncTask(mUserDao).execute(user)
    }

    private class deleteAsyncTask internal constructor(private val mAsyncTaskDao: UserDao) :
        AsyncTask<User, Void, Void>() {

        override fun doInBackground(vararg params: User): Void? {
            mAsyncTaskDao.delete(params[0])
            return null
        }
    }
}
