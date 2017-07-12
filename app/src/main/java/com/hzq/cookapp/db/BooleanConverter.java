package com.hzq.cookapp.db;

import android.arch.persistence.room.TypeConverter;

/**
 * @author: hezhiqiang
 * @date: 2017/7/5
 * @version:
 * @description:
 */

public class BooleanConverter {

    @TypeConverter
    public static Integer toInteger(Boolean select){
        return select == null || !select ? 0 : 1;
    }

    @TypeConverter
    public static Boolean fromInteger(int select){
        return select == 0 ? false : true;
    }
}
