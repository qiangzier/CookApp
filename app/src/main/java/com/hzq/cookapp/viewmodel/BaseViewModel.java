package com.hzq.cookapp.viewmodel;

import android.app.Application;
import android.app.ProgressDialog;
import android.arch.lifecycle.AndroidViewModel;

/**
 * @author: hezhiqiang
 * @date: 2017/6/24
 * @version:
 * @description:
 */

public class BaseViewModel extends AndroidViewModel {

    public BaseViewModel(Application application) {
        super(application);
    }

    public ProgressDialog progressDialog;

    public void showDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(this.getApplication());
            progressDialog.setMessage("请稍等...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    public void dismissDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }
}
