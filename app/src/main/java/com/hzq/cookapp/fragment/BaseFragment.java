package com.hzq.cookapp.fragment;

import android.arch.lifecycle.LifecycleFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hzq.cookapp.BaseActivity;
import com.hzq.cookapp.R;

import butterknife.ButterKnife;

/**
 * @author: hezhiqiang
 * @date: 2017/6/23
 * @version:
 * @description:
 */

public abstract class BaseFragment extends LifecycleFragment {

    protected BaseActivity mActivity;
    protected Bundle mArguments;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (BaseActivity) context;
        this.mArguments = getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(),container,false);
        if(isShowToolbar()) {
            initToolbar(view);
        }
        initView(view);
        ButterKnife.bind(this, view);
        return view;
    }

    public void initView(View rootView){

    }

    public boolean isShowToolbar(){
        return true;
    }

    protected abstract int getLayoutId();
    private Toolbar toolbar;
    protected void initToolbar(View rootView){
        toolbar = (Toolbar) rootView.findViewById(R.id.toolBar);
        mActivity.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        });
        if(mActivity.getSupportActionBar() != null)
            mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setTitle(String msg){
        mActivity.setTitle(msg);
    }
}
