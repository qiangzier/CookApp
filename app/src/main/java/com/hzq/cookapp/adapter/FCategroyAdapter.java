package com.hzq.cookapp.adapter;

import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hzq.cookapp.R;
import com.hzq.cookapp.databinding.CategroyItemBinding;
import com.hzq.cookapp.db.entity.CategoryEntity;
import com.hzq.cookapp.viewmodel.CategroyViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: hezhiqiang
 * @date: 2017/6/23
 * @version:
 * @description:
 */

public class FCategroyAdapter extends RecyclerView.Adapter<FCategroyAdapter.CategroyHolder>{

    private List<CategoryEntity> mDatas = new ArrayList<>();
    private CategroyViewModel mViewModel;
    private Resources resources;
    public FCategroyAdapter(CategroyViewModel viewModel){
        this.mViewModel = viewModel;
    }

    public void setData(List<CategoryEntity> list){
        mDatas.clear();
        if(list != null){
            mDatas.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public CategroyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CategroyItemBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.categroy_item,parent,false);
        resources = parent.getResources();
        return new CategroyHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(CategroyHolder holder, int position) {
        CategoryEntity categoryEntity = mDatas.get(position);
        categoryEntity.setName(pt(categoryEntity.getName()));
        holder.binding.setCategroyInfo(mDatas.get(position));
        holder.binding.setViewModel(mViewModel);
        if(TextUtils.equals(mViewModel.getSelectEntity().getCtgId(),categoryEntity.getCtgId())){
            holder.binding.layout.setBackgroundColor(resources.getColor(R.color.color_FFFFFF));
            holder.binding.label.setVisibility(View.VISIBLE);
        }else{
            holder.binding.layout.setBackgroundColor(resources.getColor(R.color.color_F5F5F5));
            holder.binding.label.setVisibility(View.GONE);
        }
    }

    private String pt(String s){
        return s.replace("按","").replace("选择菜谱","");
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    static class CategroyHolder extends RecyclerView.ViewHolder{

        final CategroyItemBinding binding;
        public CategroyHolder(CategroyItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
