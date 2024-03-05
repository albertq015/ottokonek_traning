package com.otto.mart.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PpobPurchase (
    @PrimaryKey(autoGenerate = true) var id: Int? = 0,
    @ColumnInfo(name = "product_name") var productName: String?,
    @ColumnInfo(name = "product_code") var productCode: String?,
    @ColumnInfo(name = "quantity") var quantity: Int?
)