package com.hzq.cookapp.viewmodel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;

import com.hzq.cookapp.db.CookDatabaseHelper;
import com.hzq.cookapp.db.entity.CategoryEntity;

import java.util.List;

/**
 * @author: hezhiqiang
 * @date: 2017/6/23
 * @version:
 * @description:
 */

public class CookChannelViewModel extends BaseViewModel {

    public boolean flag;

    private LiveData<List<CategoryEntity>> observableData;

    public CookChannelViewModel(Application application) {
        super(application);

        observableData = Transformations.switchMap(CookDatabaseHelper.getIsCreatedDatabase(), new Function<Boolean, LiveData<List<CategoryEntity>>>() {
            @Override
            public LiveData<List<CategoryEntity>> apply(Boolean input) {
                return CookDatabaseHelper.getCategroyDao().getCategroys();
            }
        });
    }

    public LiveData<List<CategoryEntity>> getObservableData() {
        return observableData;
    }

    public static CookChannelViewModel getInstance(FragmentActivity activity){
        CookChannelViewModel viewModel = ViewModelProviders.of(activity).get(CookChannelViewModel.class);
        return viewModel;
    }

    public void update(CategoryEntity... entity){
        flag = true;
        CookDatabaseHelper.getCategroyDao().update(entity);
    }
}
