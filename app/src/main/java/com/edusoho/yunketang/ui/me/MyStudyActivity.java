package com.edusoho.yunketang.ui.me;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.adapter.CommonViewPagerAdapter;
import com.edusoho.yunketang.base.BaseActivity;
import com.edusoho.yunketang.base.MagicIndicatorBuilder;
import com.edusoho.yunketang.base.MagicIndicatorPageListener;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.databinding.ActivityMyStudyBinding;
import com.edusoho.yunketang.utils.DensityUtil;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

@Layout(value = R.layout.activity_my_study, title = "我的学习")
public class MyStudyActivity extends BaseActivity<ActivityMyStudyBinding> {

    private VideoStudyFragment videoStudyFragment = new VideoStudyFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        MagicIndicatorBuilder.MagicIndicatorConfiguration configuration = new MagicIndicatorBuilder.MagicIndicatorConfiguration(this);
        configuration.labels = new String[]{"视频"};
        configuration.labelTextSize = 16;
        configuration.textStyle = Typeface.BOLD;
        configuration.titleNormalColor = R.color.text_black;
        configuration.lineMode = LinePagerIndicator.MODE_EXACTLY;
        configuration.lineHeight = DensityUtil.dip2px(this, 2f);
        configuration.lineWidth = DensityUtil.dip2px(this, 50f);
        // init ViewPager
        getDataBinding().vpMain.setOffscreenPageLimit(1);
        getDataBinding().vpMain.setAdapter(new CommonViewPagerAdapter(getSupportFragmentManager(), videoStudyFragment));
        getDataBinding().vpMain.addOnPageChangeListener(new MagicIndicatorPageListener(getDataBinding().mainTabIndicator));
        // set MagicIndicator
        CommonNavigator commonNavigator = MagicIndicatorBuilder.buildCommonNavigator(this, configuration, new MagicIndicatorBuilder.OnNavigatorClickListener() {
            @Override
            public void onNavigatorClickListener(int index) {
                getDataBinding().vpMain.setCurrentItem(index, true);
            }
        });
        getDataBinding().mainTabIndicator.setNavigator(commonNavigator);
    }
}
