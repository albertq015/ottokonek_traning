package com.otto.mart.presenter.dao.db;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.content.Context;

import com.otto.mart.model.db.City;
import com.otto.mart.model.db.District;
import com.otto.mart.model.db.Province;
import com.otto.mart.support.util.db.AssetSQLiteOpenHelperFactory;

@Database(entities = {Province.class, City.class, District.class}, version = 3, exportSchema = false)
public abstract class AddressDatabase extends RoomDatabase {
    public abstract AddressDao addressDao();

    private static volatile AddressDatabase INSTANCE;

    public static AddressDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AddressDatabase.class) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AddressDatabase.class, "address.db")
                        .openHelperFactory(new AssetSQLiteOpenHelperFactory())
                        .build();
            }

        }
        return INSTANCE;
    }

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {

            db.execSQL("CREATE TABLE IF NOT EXISTS `province_temp` (" +
                    "`table_id` NOT NULL PRIMARY KEY,"+
                    "`id` text NOT NULL," +
                    "`name` text NOT NULL)");

            db.execSQL("INSERT INTO `provice_temp` (`id`, `name`)" +
                    "SELECT `id`, `name` FROM province");

            db.execSQL("DROP TABLE provice");

            db.execSQL("ALTER TABLE `provice_temp` RENAME TO `province`");
        }
    };
}
