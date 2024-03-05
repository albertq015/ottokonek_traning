package com.otto.mart.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.otto.mart.database.entities.User


@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): User

    @Insert
    fun insert(vararg users: User)

    @Delete
    fun delete(user: User)

    @Update
    fun update(user: User)

    @Query("DELETE FROM user")
    fun deleteAll()

    @Query("SELECT * from user ORDER BY id DESC")
    fun getAllUsers(): LiveData<List<User>>
}