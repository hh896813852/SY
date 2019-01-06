package com.edusoho.yunketang.ui.launcher;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.base.BaseFragment;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.databinding.FragmentGuideBinding;
import com.edusoho.yunketang.helper.AppPreferences;
import com.edusoho.yunketang.ui.MainTabActivity;

/**
 * 引导页内容
 */
@Layout(R.layout.fragment_guide)
public class GuideFragment extends BaseFragment<FragmentGuideBinding> {
    // DataBinding
    public ObservableField<Integer> guidePagerColor = new ObservableField<>(0); // 引导页背景色
    public ObservableField<Drawable> imageResource = new ObservableField<>();   // 引导页图片资源
    public ObservableBoolean lastGuidePager = new ObservableBoolean();          // 是否是最后一张引导页

    /**
     * 获取引导页Fragment实例
     *
     * @param imgRes          引导页图片资源id
     * @param guidePagerColor 引导页背景色
     * @param isLastPager     是否是最后一张引导页
     * @return
     */
    public static GuideFragment newInstance(int imgRes, int guidePagerColor, boolean isLastPager) {
        GuideFragment fragment = new GuideFragment();
        Bundle args = new Bundle();
        args.putBoolean("isLastPager", isLastPager);
        args.putInt("imageResource", imgRes);
        args.putInt("guidePagerColor", guidePagerColor);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lastGuidePager.set(getArguments().getBoolean("isLastPager"));
        imageResource.set(ContextCompat.getDrawable(getSupportedActivity(), getArguments().getInt("imageResource")));
        guidePagerColor.set(getArguments().getInt("guidePagerColor"));
    }

    public View.OnClickListener goPlayCommand = v -> {
        AppPreferences.finishFirstOpenApp();
        startActivity(new Intent(getActivity(), MainTabActivity.class));
        if (getActivity() != null) {
            ((GuideActivity) getActivity()).overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
        }
        getActivity().finish();
    };
}
