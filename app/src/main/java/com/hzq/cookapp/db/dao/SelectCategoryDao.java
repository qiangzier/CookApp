package com.hzq.cookapp.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.hzq.cookapp.db.entity.SelectCategoryEntity;

import java.util.List;

/**
 * @author: hezhiqiang
 * @date: 2017/6/30
 * @version:
 * @description:
 */

@Dao
public interface SelectCategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SelectCategoryEntity... entities);

    @Insert
    void insert(List<SelectCategoryEntity> entities);

    @Query("select * from select_category_tab")
    LiveData<List<SelectCategoryEntity>> getCategroys();

    @Query("select * from select_category_tab")
    List<SelectCategoryEntity> getCategroys1();
}

