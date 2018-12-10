package com.edusoho.yunketang.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.edusoho.yunketang.base.BaseFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author any on 2016/7/16.
 */
public class CommonViewPagerAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> fragments;

    public CommonViewPagerAdapter(FragmentManager fm, ArrayList<BaseFragment> fragmentList) {
        super(fm);
        this.fragments = fragmentList;
    }

    public CommonViewPagerAdapter(FragmentManager fm, BaseFragment... fragmentsArray) {
        super(fm);
        fragments = new ArrayList<>();
        fragments.addAll(Arrays.asList(fragmentsArray));
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}