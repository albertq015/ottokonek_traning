package com.otto.mart.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.otto.mart.database.entities.PpobPurchase


@Dao
interface PpobPurchaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg ppobPurchase: PpobPurchase)

    @Query("SELECT * from ppobpurchase WHERE product_name = (:productName) AND product_code = (:productCode)")
    fun getProduct(productName: String, productCode: String): LiveData<List<PpobPurchase>>

    @Query("SELECT * from ppobpurchase WHERE product_name = (:productName) ORDER BY quantity DESC LIMIT 1")
    fun getProductByName(productName: String): LiveData<List<PpobPurchase>>
}