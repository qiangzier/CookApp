package com.hzq.cookapp.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hzq.cookapp.R;
import com.hzq.cookapp.callback.ClickCallback;
import com.hzq.cookapp.databinding.SCategroyItemBinding;
import com.hzq.cookapp.db.entity.CategoryEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: hezhiqiang
 * @date: 2017/6/23
 * @version:
 * @description:
 */

public class SCategroyAdapter extends RecyclerView.Adapter<SCategroyAdapter.CategroyHolder>{

    private List<CategoryEntity> mDatas = new ArrayList<>();

    public void setData(List<CategoryEntity> list){
        mDatas.clear();
        if(list != null){
            mDatas.addAll(list);
        }
        notifyDataSetChanged();
    }

    private ClickCallback<CategoryEntity> clickCallback;

    public void setClickCallback(ClickCallback<CategoryEntity> clickCallback) {
        this.clickCallback = clickCallback;
    }

    @Override
    public CategroyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SCategroyItemBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.s_categroy_item,parent,false);
        return new CategroyHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(CategroyHolder holder, int position) {
        holder.binding.setCategroyInfo(mDatas.get(position));
        holder.binding.setCallback(clickCallback);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    static class CategroyHolder extends RecyclerView.ViewHolder{

        final SCategroyItemBinding binding;
        public CategroyHolder(SCategroyItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
