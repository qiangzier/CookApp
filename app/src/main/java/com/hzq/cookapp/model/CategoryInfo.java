package com.hzq.cookapp.model;

import com.hzq.cookapp.db.entity.CategoryEntity;

import java.io.Serializable;
import java.util.List;

/**
 * @author: hezhiqiang
 * @date: 2017/6/22
 * @version:
 * @description: 分类信息
 */

public class CategoryInfo implements Serializable{

    private static final long serialVersionUID = 8992312472516636312L;
    public CategoryEntity categoryInfo;
    public List<CategoryInfo> childs;

}
