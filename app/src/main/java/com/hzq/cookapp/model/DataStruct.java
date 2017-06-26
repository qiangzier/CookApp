package com.hzq.cookapp.model;

/**
 * @author: hezhiqiang
 * @date: 2017/6/25
 * @version:
 * @description:
 */

public class DataStruct {
    public int type;
    public Recipe.Method method;
    public String title;
    public boolean isFirst;

    public DataStruct(int type, Recipe.Method method, String title) {
        this.type = type;
        this.method = method;
        this.title = title;
    }
}
