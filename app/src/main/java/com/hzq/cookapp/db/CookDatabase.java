package com.hzq.cookapp.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.hzq.cookapp.db.dao.CategroyDao;
import com.hzq.cookapp.db.entity.CategoryEntity;

/**
 * @author: hezhiqiang
 * @date: 2017/6/23
 * @version:
 * @description:
 */
@Database(entities = {CategoryEntity.class},version = 1)
@TypeConverters(BooleanConverter.class)
public abstract  class CookDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "cook_categroy_db";

    public abstract CategroyDao getCategrouDao();

}
