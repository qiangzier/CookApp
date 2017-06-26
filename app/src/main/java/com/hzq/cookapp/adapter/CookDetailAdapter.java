package com.hzq.cookapp.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hzq.cookapp.R;
import com.hzq.cookapp.callback.ClickCallback;
import com.hzq.cookapp.databinding.CookDetailFuncBinding;
import com.hzq.cookapp.databinding.CookDetailHeaderBinding;
import com.hzq.cookapp.databinding.CookDetailStepBinding;
import com.hzq.cookapp.model.CookModel;
import com.hzq.cookapp.model.DataStruct;
import com.hzq.cookapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: hezhiqiang
 * @date: 2017/6/24
 * @version:
 * @description:
 */

public class CookDetailAdapter extends RecyclerView.Adapter<CookDetailAdapter.ItemViewHolder> {

    private final static int Cook_Detail_Item_Type_Title = 0;
    private final static int Cook_Detail_Item_Type_Header = 1;
    private final static int Cook_Detail_Item_Type_Step = 2;

    private List<DataStruct> mData;
    private ClickCallback<CookModel> click;
    private Recipe recipe;

    public void setClick(ClickCallback<CookModel> click) {
        this.click = click;
    }

    public CookDetailAdapter(Recipe recipe){
        this.recipe = recipe;
        mData = new ArrayList<>();

        if(this.recipe != null){
            mData.add(new DataStruct(Cook_Detail_Item_Type_Title,null,recipe.sumary));
        }

        if(recipe.getIngredientsData() != null){
            boolean flag = false;
            DataStruct m = null;
            for (String s : recipe.getIngredientsData()) {
                m = new DataStruct(Cook_Detail_Item_Type_Header,null,s);
                if(!flag){
                    m.isFirst = true;
                }
                flag = true;
                mData.add(m);
            }
        }

        if(recipe.getMethodList() != null){
            DataStruct m = null;
            boolean flag = false;
            for (Recipe.Method method : recipe.getMethodList()) {
                m = new DataStruct(Cook_Detail_Item_Type_Step,method,null);
                if(!flag)
                    m.isFirst = true;
                flag = true;
                mData.add(m);
            }
        }
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder holder = null;
        if(Cook_Detail_Item_Type_Title == viewType){
            CookDetailFuncBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()), R.layout.cook_detail_func,parent,false);
            holder = new FuncViewHolder(binding);
        }else if(Cook_Detail_Item_Type_Header == viewType){
            CookDetailHeaderBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()), R.layout.cook_detail_use,parent,false);
            holder = new HeaderViewHolder(binding);
        }else if(Cook_Detail_Item_Type_Step == viewType){
            CookDetailStepBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()), R.layout.cook_detail_step,parent,false);
            holder = new StepViewHolder(binding);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        DataStruct model = mData.get(position);
        if(model.type == Cook_Detail_Item_Type_Title){
            FuncViewHolder vh = (FuncViewHolder) holder;
            vh.binding.setModel(model);
        }else if(model.type == Cook_Detail_Item_Type_Header){
            HeaderViewHolder vh = (HeaderViewHolder) holder;
            vh.binding.setModel(model);
            vh.binding.name.setVisibility(model.isFirst ? View.VISIBLE : View.GONE);
        }else if(model.type == Cook_Detail_Item_Type_Step){
            StepViewHolder vh = (StepViewHolder) holder;
            vh.binding.setModel(model);
            vh.binding.name.setVisibility(model.isFirst ? View.VISIBLE : View.GONE);
            if(!TextUtils.isEmpty(model.method.img)) {
                vh.binding.image.setImageURI(model.method.img);
                vh.binding.image.setVisibility(View.VISIBLE);
            }else {
                vh.binding.image.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).type;
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    private class HeaderViewHolder extends ItemViewHolder{
        CookDetailHeaderBinding binding;
        public HeaderViewHolder(CookDetailHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private class FuncViewHolder extends ItemViewHolder{
        CookDetailFuncBinding binding;
        public FuncViewHolder(CookDetailFuncBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private class StepViewHolder extends ItemViewHolder{
        CookDetailStepBinding binding;
        public StepViewHolder(CookDetailStepBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{
        public ItemViewHolder(View itemView) {
            super(itemView);
        }
    }
}
