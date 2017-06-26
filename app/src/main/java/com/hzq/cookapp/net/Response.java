package com.hzq.cookapp.net;

import java.io.Serializable;

/**
 * @author: hezhiqiang
 * @date: 2017/6/22
 * @version:
 * @description:
 */

public class Response<T> implements Serializable{
    private static final long serialVersionUID = -5584682184447227914L;

    public String msg;
    public String retCode;
    public T result;
}
