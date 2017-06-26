package com.hzq.cookapp.model;

import java.io.Serializable;

/**
 * @author: hezhiqiang
 * @date: 2017/6/21
 * @version:
 * @description:
 */

public class CookModel implements Serializable{
    private static final long serialVersionUID = -5409032842397319611L;

    public Long[] ctgIds;       //分类ID
    public String ctgTitles;    //分类title；荤菜,炒,浙菜,儿童食谱,养生
    public String menuId;       //菜谱id：00100010070000000001"
    public String name;         //菜谱名称
    public String thumbnail;    //预览图地址
    public Recipe recipe;       //制作步骤

}
