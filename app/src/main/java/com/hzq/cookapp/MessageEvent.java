package com.hzq.cookapp;

/**
 * @author: hezhiqiang
 * @date: 2017/7/12
 * @version:
 * @description:
 */

public class MessageEvent {

    public static final int CLICK_MODE = 0;
    public static final int REFRESH_MODE = 1;
    public int type;
    public int position;

    public  MessageEvent(int position){
        this.position = position;
    }

    public MessageEvent(int type, int position) {
        this.type = type;
        this.position = position;
    }
}
