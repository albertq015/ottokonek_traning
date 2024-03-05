package com.otto.mart.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "city")
public class City extends MainModule {

    @ColumnInfo(name = "province_id")
    private String provinceId;

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }
}
