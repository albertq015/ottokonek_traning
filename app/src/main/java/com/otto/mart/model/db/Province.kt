package com.otto.mart.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "province")
data class Province(
        @PrimaryKey
        @ColumnInfo(name = "id")
        @NotNull
        var id: String,

        @ColumnInfo(name = "name")
        var name: String?)
