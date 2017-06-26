package com.hzq.cookapp.callback;

/**
 * @author: hezhiqiang
 * @date: 2017/6/23
 * @version:
 * @description:
 */

public interface Callback<T> {
    void call(T t);
}
