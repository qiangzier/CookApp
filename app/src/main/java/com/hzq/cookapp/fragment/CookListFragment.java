package com.hzq.cookapp.fragment;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hzq.cookapp.CookDetailActivity;
import com.hzq.cookapp.R;
import com.hzq.cookapp.adapter.CookListAdapter;
import com.hzq.cookapp.callback.ClickCallback;
import com.hzq.cookapp.model.CookModel;
import com.hzq.cookapp.viewmodel.CookListViewModel;

import java.util.List;

/**
 * @author: hezhiqiang
 * @date: 2017/6/24
 * @version:
 * @description:
 */

public class CookListFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private CookListAdapter adapter = null;
    private String cid;

    @Override
    protected int getLayoutId() {
        return R.layout.cook_list_layout;
    }

    @Override
    public void initView(View rootView) {
        adapter = new CookListAdapter();
        adapter.setClick(new ClickCallback<CookModel>() {
            @Override
            public void click(CookModel t) {
                CookDetailActivity.startAction(mActivity,t);
            }
        });
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);

        cid = mArguments.getString("cid");
        setTitle(mArguments.getString("name"));

        CookListViewModel.getInstance(mActivity,cid).
                getObserableList().observe(this, new Observer<List<CookModel>>() {
            @Override
            public void onChanged(@Nullable List<CookModel> cookModels) {
                adapter.setData(cookModels);
            }
        });
    }

}
