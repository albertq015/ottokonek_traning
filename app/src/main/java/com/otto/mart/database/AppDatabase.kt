package com.otto.mart.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.otto.mart.BuildConfig
import com.otto.mart.database.dao.PpobPurchaseDao
import com.otto.mart.database.dao.UserDao
import com.otto.mart.database.entities.PpobPurchase
import com.otto.mart.database.entities.User

@Database(entities = arrayOf(User::class, PpobPurchase::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun ppobPurchaseDao(): PpobPurchaseDao
    abstract fun userDao(): UserDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val DB_NAME = BuildConfig.APPLICATION_ID + "_database"

        internal fun getDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder<AppDatabase>(
                            context.applicationContext,
                            AppDatabase::class.java, DB_NAME
                        )
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}
