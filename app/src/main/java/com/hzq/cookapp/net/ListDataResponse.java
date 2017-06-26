package com.hzq.cookapp.net;

import java.util.List;

/**
 * @author: hezhiqiang
 * @date: 2017/6/22
 * @version:
 * @description:
 */

public class ListDataResponse<T> {
    public int curPage;
    public int total;
    public List<T> list;
}
