package com.hzq.cookapp.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.hzq.cookapp.db.entity.CategoryEntity;

import java.util.List;

/**
 * @author: hezhiqiang
 * @date: 2017/6/23
 * @version:
 * @description:
 */

@Dao
public interface CategroyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CategoryEntity... entities);

    @Insert
    void insert(List<CategoryEntity> entities);

    @Query("select * from cook_categroy where parentId= :id")
    LiveData<List<CategoryEntity>> getCategroyByPId(String id);

    @Query("select * from cook_categroy")
    LiveData<List<CategoryEntity>> getCategroys();

    @Query("select * from cook_categroy")
    List<CategoryEntity> getCategroys1();



}
