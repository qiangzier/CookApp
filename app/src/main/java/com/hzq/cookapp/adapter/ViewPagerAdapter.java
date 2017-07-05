package com.hzq.cookapp.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hzq.cookapp.R;
import com.hzq.cookapp.db.entity.SelectCategoryEntity;
import com.hzq.cookapp.fragment.CookListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: hezhiqiang
 * @date: 2017/6/30
 * @version:
 * @description:
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<SelectCategoryEntity> categroyData = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm,List<SelectCategoryEntity> list) {
        super(fm);
        this.categroyData = list;
    }

    public void setCategroyData(List<SelectCategoryEntity> categroyData) {
        this.categroyData = categroyData;
        notifyDataSetChanged();
    }

    public List<SelectCategoryEntity> getCategroyData() {
        return categroyData;
    }

    @Override
    public Fragment getItem(int position) {
        CookListFragment cookListFragment = new CookListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("cid",categroyData.get(position).getCtgId());
        bundle.putString("name",categroyData.get(position).getName());
        bundle.putInt("waveColor", R.color.color_F5F5F5);
        cookListFragment.setArguments(bundle);
        return cookListFragment;
    }

    @Override
    public int getCount() {
        if(categroyData != null)
            return categroyData.size();
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return categroyData.get(position).hashCode();
    }

    public CharSequence getPageTitle(int position){
        return categroyData.get(position).getName();
    }
}
