package com.hzq.cookapp.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hzq.cookapp.CookListActivity;
import com.hzq.cookapp.R;
import com.hzq.cookapp.adapter.FCategroyAdapter;
import com.hzq.cookapp.adapter.SCategroyAdapter;
import com.hzq.cookapp.callback.ClickCallback;
import com.hzq.cookapp.db.entity.CategoryEntity;
import com.hzq.cookapp.viewmodel.CategroyViewModel;

import java.util.List;

import butterknife.BindView;

/**
 * @author: hezhiqiang
 * @date: 2017/6/23
 * @version:
 * @description:
 */

public class CategroyFragment extends BaseFragment {
    @BindView(R.id.recyclerViewF)
    RecyclerView recyclerViewF;
    @BindView(R.id.recyclerViewS)
    RecyclerView recyclerViewS;

    private String cid = "-100";
    private FCategroyAdapter fAdapter;
    private SCategroyAdapter sAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.categroy_layout;
    }

    @Override
    public void initView(View rootView) {
        setTitle("分类");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final CategroyViewModel viewModel =
                ViewModelProviders.of(this, new CategroyViewModel.Factory(getActivity().getApplication(), cid)).get(CategroyViewModel.class);
        fAdapter = new FCategroyAdapter(viewModel);
        recyclerViewF.setAdapter(fAdapter);

        sAdapter = new SCategroyAdapter();
        recyclerViewS.setAdapter(sAdapter);

        viewModel.getObservableDataF().observe(this, new Observer<List<CategoryEntity>>() {
            @Override
            public void onChanged(@Nullable final List<CategoryEntity> categoryEntities) {
                fAdapter.setData(categoryEntities);
            }
        });

        viewModel.getObservableDataS().observe(this, new Observer<List<CategoryEntity>>() {
            @Override
            public void onChanged(@Nullable List<CategoryEntity> entities) {
                sAdapter.setData(entities);
            }
        });

        sAdapter.setClickCallback(new ClickCallback<CategoryEntity>() {
            @Override
            public void click(CategoryEntity t) {
                CookListActivity.startAction(mActivity,t.getCtgId(),t.getName());
            }
        });
    }

}
