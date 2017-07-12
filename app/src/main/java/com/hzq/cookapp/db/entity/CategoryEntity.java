package com.hzq.cookapp.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * @author: hezhiqiang
 * @date: 2017/6/23
 * @version:
 * @description:
 */

@Entity(tableName = "cook_categroy")
public class CategoryEntity {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String ctgId;        //0010001002",
    private String name;         //按菜品选择菜谱",
    private String parentId;     //0010001001"
    private boolean isAddMyChannel;      //是否在首页展示
    private int orderIndex;           //排序

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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean isAddMyChannel() {
        return isAddMyChannel;
    }

    public void setAddMyChannel(boolean addMyChannel) {
        isAddMyChannel = addMyChannel;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }
}
