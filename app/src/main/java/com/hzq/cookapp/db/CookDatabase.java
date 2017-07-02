package com.hzq.cookapp.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.hzq.cookapp.db.dao.CategroyDao;
import com.hzq.cookapp.db.dao.SelectCategoryDao;
import com.hzq.cookapp.db.entity.CategoryEntity;
import com.hzq.cookapp.db.entity.SelectCategoryEntity;

/**
 * @author: hezhiqiang
 * @date: 2017/6/23
 * @version:
 * @description:
 */
@Database(entities = {CategoryEntity.class, SelectCategoryEntity.class},version = 1)
public abstract  class CookDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "cook_categroy_db";

    public abstract CategroyDao getCategrouDao();

    public abstract SelectCategoryDao getSelectCategoryDao();
}
