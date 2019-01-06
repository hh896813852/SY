package com.edusoho.yunketang.ui.launcher;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.adapter.CommonViewPagerAdapter;
import com.edusoho.yunketang.base.BaseActivity;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.base.annotation.Translucent;
import com.edusoho.yunketang.databinding.ActivityGuideBinding;
import com.edusoho.yunketang.utils.LogUtil;
import com.edusoho.yunketang.utils.ScreenUtil;
import com.youth.banner.Transformer;

/**
 * @author huhao on 2018/8/22
 */
@Translucent
@Layout(R.layout.activity_guide)
public class GuideActivity extends BaseActivity<ActivityGuideBinding> {
    private int screenW;
    private ImageView checkedView;
    private LinearLayout.LayoutParams layoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenW = ScreenUtil.getScreenWidth(this);
        // 初始化小圆点
        intDos();
        // init ViewPager
        GuideFragment page1 = GuideFragment.newInstance(R.drawable.bg_guide_1, 0xffffffff, false);
        GuideFragment page2 = GuideFragment.newInstance(R.drawable.bg_guide_2, 0xffffffff, false);
        GuideFragment page3 = GuideFragment.newInstance(R.drawable.bg_guide_3, 0xffffffff, true);
        try {
            getDataBinding().vpGuide.setPageTransformer(true, Transformer.Tablet.newInstance());
        } catch (Exception e) {
            LogUtil.e("Please set the PageTransformer class");
        }
        getDataBinding().vpGuide.setAdapter(new CommonViewPagerAdapter(getSupportFragmentManager(), page1, page2, page3));
        getDataBinding().vpGuide.setOnScrollListener(this::onScrollChanged);
    }

    /**
     * 初始化小圆点
     */
    private void intDos() {
        // 3个固定点
        for (int i = 0; i < 3; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.icon_dos_normal);
            // 设置空间大小
            LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(24, 24);
            // 设置宽高
            Params.setMargins(10, 0, 10, 0);
            // 把小圆点放到页面的LinearLayout容器中
            getDataBinding().layout.addView(imageView, Params);
        }

        // 一个移动点
        checkedView = new ImageView(this);
        checkedView.setImageResource(R.drawable.icon_dos_checked);
        // 设置空间大小
        layoutParams = new LinearLayout.LayoutParams(24, 24);
        // 设置margin
        layoutParams.setMargins(screenW / 2 - (12 + 20 + 24), 0, 0, 0);
        getDataBinding().checkedLayout.addView(checkedView, layoutParams);
    }

    /**
     * 小圆点随ViewPager滑动
     */
    private void onScrollChanged(int scrollX) {
        // 设置marginLeft
        layoutParams.setMargins(screenW / 2 - (12 + 20 + 24) + (scrollX * 44 / screenW), 0, 0, 0);
        checkedView.setLayoutParams(layoutParams);

        // 第三张不显示小圆点
//        if ((scrollX - screenW) > 0) {
//            getDataBinding().layout.setVisibility(View.GONE);
//            getDataBinding().checkedLayout.setVisibility(View.GONE);
//        } else {
//            getDataBinding().layout.setVisibility(View.VISIBLE);
//            getDataBinding().checkedLayout.setVisibility(View.VISIBLE);
//        }
    }
}
