package com.hzq.cookapp.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * @author: hezhiqiang
 * @date: 2017/6/30
 * @version:
 * @description:
 */

@Entity(tableName = "select_category_tab")
public class SelectCategoryEntity {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String ctgId;        //0010001002",
    private String name;         //按菜品选择菜谱",

    public SelectCategoryEntity(String ctgId, String name) {
        this.ctgId = ctgId;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCtgId() {
        return ctgId;
    }

    public void setCtgId(String ctgId) {
        this.ctgId = ctgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
