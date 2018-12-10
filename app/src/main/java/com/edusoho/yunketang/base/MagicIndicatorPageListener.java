package com.edusoho.yunketang.base;

import android.support.v4.view.ViewPager;

import net.lucode.hackware.magicindicator.MagicIndicator;

/**
 * Created by any on 16/7/26.
 */
public class MagicIndicatorPageListener implements ViewPager.OnPageChangeListener {
    MagicIndicator magicIndicator;


    public MagicIndicatorPageListener(MagicIndicator magicIndicator) {
        this.magicIndicator = magicIndicator;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        magicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        magicIndicator.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        magicIndicator.onPageScrollStateChanged(state);
    }
}