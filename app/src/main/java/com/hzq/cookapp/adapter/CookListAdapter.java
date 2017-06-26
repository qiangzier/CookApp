package com.hzq.cookapp.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hzq.cookapp.R;
import com.hzq.cookapp.callback.ClickCallback;
import com.hzq.cookapp.databinding.CookListItemBinding;
import com.hzq.cookapp.model.CookModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: hezhiqiang
 * @date: 2017/6/24
 * @version:
 * @description:
 */

public class CookListAdapter extends RecyclerView.Adapter<CookListAdapter.ViewHolder> {

    private List<CookModel> mData;
    private ClickCallback<CookModel> click;

    public void setClick(ClickCallback<CookModel> click) {
        this.click = click;
    }

    public CookListAdapter(){
        mData = new ArrayList<>();
    }

    public void setData(List<CookModel> models){
        mData.clear();
        if(models != null)
            mData.addAll(models);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CookListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.cook_list_item,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mBinding.setCookModel(mData.get(position));
        holder.mBinding.image.setImageURI(mData.get(position).recipe.img);
        holder.mBinding.setCallback(click);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CookListItemBinding mBinding;
        public ViewHolder(CookListItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
