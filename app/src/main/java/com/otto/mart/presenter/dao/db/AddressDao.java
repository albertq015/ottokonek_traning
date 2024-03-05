package com.otto.mart.presenter.dao.db;

import androidx.room.Dao;
import androidx.room.Query;

import com.otto.mart.model.db.City;
import com.otto.mart.model.db.District;
import com.otto.mart.model.db.Province;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface AddressDao {

    @Query("SELECT * FROM province ORDER BY name ASC")
    Single<List<Province>> getAllProvince();

    @Query("SELECT * FROM city WHERE province_id = :cityId ORDER BY name ASC")
    Single<List<City>> getCity(String cityId);

    @Query("SELECT * FROM district WHERE city_id = :districtId ORDER BY name ASC")
    Single<List<District>> getDistrict(String districtId);
}
