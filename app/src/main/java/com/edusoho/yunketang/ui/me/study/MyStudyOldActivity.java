package com.edusoho.yunketang.ui.me.study;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.widget.TextView;

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

import java.util.List;

@Layout(value = R.layout.activity_my_study, title = "我的学习")
public class MyStudyOldActivity extends BaseActivity<ActivityMyStudyBinding> {

    private CourseStudyFragment courseStudyFragment = new CourseStudyFragment();
    private LiveStudyFragment liveStudyFragment = new LiveStudyFragment();
    private ClassStudyFragment classStudyFragment = new ClassStudyFragment();

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
        configuration.labels = new String[]{"课程", "直播", "班级"};
        configuration.labelTextSize = 15;
        configuration.titleNormalColor = R.color.text_black;
        configuration.lineMode = LinePagerIndicator.MODE_EXACTLY;
        configuration.lineHeight = DensityUtil.dip2px(this, 2f);
        configuration.lineWidth = DensityUtil.dip2px(this, 50f);
        // init ViewPager
        getDataBinding().vpMain.setOffscreenPageLimit(1);
        getDataBinding().vpMain.setAdapter(new CommonViewPagerAdapter(getSupportFragmentManager(), courseStudyFragment, liveStudyFragment, classStudyFragment));
        getDataBinding().vpMain.addOnPageChangeListener(new MagicIndicatorPageListener(getDataBinding().mainTabIndicator));
        // set MagicIndicator
        CommonNavigator commonNavigator = MagicIndicatorBuilder.buildCommonNavigator2(this, configuration, new MagicIndicatorBuilder.OnNavigatorClickListener2() {
            @Override
            public void onNavigatorClickListener2(int index, List<TextView> textViews) {
                for (int i = 0; i < textViews.size(); i++) {
                    if (index == i) {
                        textViews.get(i).setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        textViews.get(i).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    } else {
                        textViews.get(i).setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                        textViews.get(i).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    }
                }
                getDataBinding().vpMain.setCurrentItem(index, true);
            }
        });
        getDataBinding().mainTabIndicator.setNavigator(commonNavigator);
    }
}
