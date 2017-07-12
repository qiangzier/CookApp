package com.hzq.cookapp;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;

import com.hzq.cookapp.adapter.CookChannelAdapter;
import com.hzq.cookapp.db.entity.CategoryEntity;
import com.hzq.cookapp.fragment.CategroyFragment;
import com.hzq.cookapp.helper.ItemDragHelperCallback;
import com.hzq.cookapp.viewmodel.CookChannelViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author: hezhiqiang
 * @date: 2017/7/5
 * @version:
 * @description:
 */

public class CookChannelActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    CookChannelAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cook_channel_layout);
        ButterKnife.bind(this);
        initToolbar();
        setTitle("菜单管理");

        GridLayoutManager manager = new GridLayoutManager(this, 4);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = adapter.getItemViewType(position);
                return (CookChannelAdapter.TYPE_MY_CHANNEL_HEADER == viewType ||
                        viewType == CookChannelAdapter.TYPE_OTHER_CHANNEL_HEADER) ? 4 : 1;
            }
        });
        adapter = new CookChannelAdapter();
        ItemDragHelperCallback callback = new ItemDragHelperCallback(adapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        adapter.setItemTouchHelper(helper);
        helper.attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        CookChannelViewModel.getInstance(this).getObservableData().observe(this, new Observer<List<CategoryEntity>>() {
            @Override
            public void onChanged(@Nullable List<CategoryEntity> entities) {
                if(CookChannelViewModel.getInstance(CookChannelActivity.this).flag){
                    CookChannelViewModel.getInstance(CookChannelActivity.this).flag = false;
                    return;
                }
                adapter.setData(entities);
            }
        });
        adapter.setViewModel(CookChannelViewModel.getInstance(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_ok){
            GeneratorActivity.startAction(this, CategroyFragment.class,"");
        }
        return super.onOptionsItemSelected(item);
    }

    public static void startAction(Context context){
        context.startActivity(new Intent(context,CookChannelActivity.class));
    }
}
