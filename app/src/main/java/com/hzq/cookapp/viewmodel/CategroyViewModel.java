package com.hzq.cookapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.text.TextUtils;

import com.hzq.cookapp.callback.Callback;
import com.hzq.cookapp.callback.ClickCallback;
import com.hzq.cookapp.db.entity.CategoryEntity;
import com.hzq.cookapp.utils.CategroyDataStore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: hezhiqiang
 * @date: 2017/6/23
 * @version:
 * @description:
 */

public class CategroyViewModel extends BaseViewModel {
    private String categroyId;
    private MutableLiveData<List<CategoryEntity>> observableDataF;
    private MutableLiveData<List<CategoryEntity>> observableDataS;
    private List<CategoryEntity> sourceData;
    private CategoryEntity selectEntity;
    public ClickCallback<CategoryEntity> fClick = new ClickCallback<CategoryEntity>() {
        @Override
        public void click(CategoryEntity t) {
            selectEntity = t;
            observableDataS.setValue(searchByCid(t.getCtgId()));
            observableDataF.setValue(searchByCid(t.getParentId()));
        }
    };

    public ClickCallback<CategoryEntity> sClick;
    public CategroyViewModel(Application application,String cid) {
        super(application);
        this.categroyId = cid;
        observableDataF = new MutableLiveData<>();
        observableDataS = new MutableLiveData<>();
        CategroyDataStore.getCategroys(new Callback<List<CategoryEntity>>() {
            @Override
            public void call(List<CategoryEntity> entities) {
                sourceData = entities;
                List<CategoryEntity> fList = searchByCid("-100");

                observableDataF.setValue(fList);
                if(fList != null && fList.size() > 1){
                    selectEntity = fList.get(0);
                    observableDataS.setValue(searchByCid(selectEntity.getCtgId()));
                }
            }
        });
    }

    public CategoryEntity getSelectEntity() {
        return selectEntity;
    }

    private List<CategoryEntity> searchByCid(String pid){
        List<CategoryEntity> list = new ArrayList<>();
        for (CategoryEntity categoryEntity : sourceData) {
            if(TextUtils.equals(categoryEntity.getParentId(),pid)){
                list.add(categoryEntity);
            }
        }
        return list;
    }

    public MutableLiveData<List<CategoryEntity>> getObservableDataF() {
        return observableDataF;
    }

    public MutableLiveData<List<CategoryEntity>> getObservableDataS() {
        return observableDataS;
    }

    public void setsClick(ClickCallback<CategoryEntity> sClick) {
        this.sClick = sClick;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory{
        private Application application;
        private String categroyId;

        public Factory(Application application, String categroyId) {
            this.application = application;
            this.categroyId = categroyId;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new CategroyViewModel(application,categroyId);
        }
    }
}
