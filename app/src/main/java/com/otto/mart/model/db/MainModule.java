package com.otto.mart.model.db;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

public class MainModule {

    @PrimaryKey
    @ColumnInfo(name = "id")
    @NonNull
    private String id;

    @ColumnInfo(name = "name")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
