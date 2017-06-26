package com.hzq.cookapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider.NewInstanceFactory;
import android.arch.lifecycle.ViewModelProviders;

import com.hzq.cookapp.BaseActivity;
import com.hzq.cookapp.model.CookModel;
import com.hzq.cookapp.net.ListDataResponse;
import com.hzq.cookapp.net.NetHelper;
import com.hzq.cookapp.net.RequestCall;

import java.util.List;

/**
 * @author: hezhiqiang
 * @date: 2017/6/24
 * @version:
 * @description:
 */

public class CookListViewModel extends BaseViewModel {

    public String cid;
    private MutableLiveData<List<CookModel>> obserableList;
    public CookListViewModel(Application application,String cid) {
        super(application);
        this.cid = cid;

        obserableList = new MutableLiveData<>();

        NetHelper.getCooksByCId(cid,new RequestCall<ListDataResponse<CookModel>>() {
            @Override
            public void success(ListDataResponse<CookModel> cookModelListDataResponse) {
                obserableList.setValue(cookModelListDataResponse.list);
            }
        });
    }

    public MutableLiveData<List<CookModel>> getObserableList() {
        return obserableList;
    }

    public static CookListViewModel getInstance(BaseActivity activity, String cid){
        Factory factory = new Factory(activity.getApplication(),cid);
        CookListViewModel viewModel =
                ViewModelProviders.of(activity,factory).get(CookListViewModel.class);
        return viewModel;
    }

    private static class Factory extends NewInstanceFactory{

        Application application;
        String cid;

        public Factory(Application application, String cid) {
            this.application = application;
            this.cid = cid;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new CookListViewModel(application,cid);
        }
    }
}
