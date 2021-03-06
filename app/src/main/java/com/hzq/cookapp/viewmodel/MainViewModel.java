package com.hzq.cookapp.viewmodel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;

import com.hzq.cookapp.callback.Callback;
import com.hzq.cookapp.db.CookDatabaseHelper;
import com.hzq.cookapp.db.entity.CategoryEntity;
import com.hzq.cookapp.utils.CategroyDataStore;

import java.util.List;

/**
 * @author: hezhiqiang
 * @date: 2017/6/30
 * @version:
 * @description:
 */

public class MainViewModel extends BaseViewModel {

    private LiveData<List<CategoryEntity>> observableData;
    public MainViewModel(Application application) {
        super(application);

        CategroyDataStore.getParentCateGroy(new Callback<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
            }
        });
        observableData = Transformations.switchMap(CookDatabaseHelper.getIsCreatedDatabase(),
                new Function<Boolean, LiveData<List<CategoryEntity>>>() {
            @Override
            public LiveData<List<CategoryEntity>> apply(Boolean input) {
                if(!Boolean.TRUE.equals(input)){
                    return new MutableLiveData<>();
                }
                return CookDatabaseHelper.getCategroyDao().getCategroyIsSelected(true);
            }
        });
    }

    public LiveData<List<CategoryEntity>> getObservableData() {
        return observableData;
    }

    public static MainViewModel getInstance(FragmentActivity activity){
        MainViewModel viewModel = ViewModelProviders.of(activity).get(MainViewModel.class);
        return viewModel;
    }
}
