package com.hzq.cookapp.db;

import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.hzq.cookapp.db.dao.CategroyDao;

/**
 * @author: hezhiqiang
 * @date: 2017/6/23
 * @version:
 * @description:
 */

public class CookDatabaseHelper {
    private static CookDatabase cookDatabase;
    private static MutableLiveData<Boolean> isCreatedDatabase = new MutableLiveData<>();

    private CookDatabaseHelper(){}

    public static void init(Context context){
        cookDatabase = Room.databaseBuilder(context,
                CookDatabase.class,CookDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build();
        isCreatedDatabase.setValue(true);
    }

    public static CookDatabase getCookDatabase() {
        return cookDatabase;
    }

    public static MutableLiveData<Boolean> getIsCreatedDatabase() {
        return isCreatedDatabase;
    }

    public static CategroyDao getCategroyDao(){
        return cookDatabase.getCategrouDao();
    }

}
