package com.otto.mart.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "district")
public class District extends MainModule {

    @ColumnInfo(name = "city_id")
    private String cityId;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
}
