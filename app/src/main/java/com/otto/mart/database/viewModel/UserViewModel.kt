package com.otto.mart.database.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.otto.mart.database.entities.User
import com.otto.mart.database.repository.UserRepository


class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val mRepository: UserRepository

    internal val allUsers: LiveData<List<User>>

    init {
        mRepository = UserRepository(application)
        allUsers = mRepository.allUsers!!
    }

    fun insert(user: User) {
        mRepository.insert(user)
    }

    fun update(user: User) {
        mRepository.update(user)
    }

    fun delete(user: User) {
        mRepository.delete(user)
    }
}