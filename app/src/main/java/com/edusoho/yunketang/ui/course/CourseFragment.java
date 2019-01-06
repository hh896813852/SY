package com.edusoho.yunketang.ui.course;

import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableField;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.edusoho.yunketang.utils.LogUtil;
import com.edusoho.yunketang.utils.NotchUtil;
import com.edusoho.yunketang.utils.ScreenUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.adapter.CommonViewPagerAdapter;
import com.edusoho.yunketang.adapter.SYBaseAdapter;
import com.edusoho.yunketang.base.BaseFragment;
import com.edusoho.yunketang.base.MagicIndicatorBuilder;
import com.edusoho.yunketang.base.MagicIndicatorPageListener;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.Banner;
import com.edusoho.yunketang.bean.Course;
import com.edusoho.yunketang.databinding.FragmentCourseBinding;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.utils.DensityUtil;
import com.edusoho.yunketang.utils.statusbar.StatusBarUtil;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huhao on 2018/7/4
 */
@Layout(value = R.layout.fragment_course)
public class CourseFragment extends BaseFragment<FragmentCourseBinding> {
    private List<String> bannerUrls = new ArrayList<>();
    private boolean isOnlineBannerLoaded;    // 上元在线banner是否加载完毕
    private boolean isAccountantBannerLoaded;// 上元会计banner是否加载完毕

    public ObservableField<Boolean> isMenuShowed = new ObservableField<>(false); // 导航菜单是否显示

    private ChildCourseFragment onlineCourseFragment = ChildCourseFragment.newInstance(1);
    private ChildCourseFragment accountantCourseFragment = ChildCourseFragment.newInstance(2);

    public List<Course> list = new ArrayList<>();
    public SYBaseAdapter adapter = new SYBaseAdapter();
    public AdapterView.OnItemClickListener onItemClick = (parent, view, position, id) -> {
        int itemHeight; // 点击的item到listView顶部距离
        if (list.get(position).courseType == 1) { // 上元在线
            getDataBinding().vpMain.setCurrentItem(0);
            itemHeight = onlineCourseFragment.getItemHeight(list.get(position).courseSort);
        } else { // 上元会计
            getDataBinding().vpMain.setCurrentItem(1);
            itemHeight = accountantCourseFragment.getItemHeight(list.get(position).courseSort);
        }
        getDataBinding().scrollView.smoothScrollTo(0, itemHeight + ScreenUtil.getScreenWidth(getSupportedActivity()) * 9 / 16 + DensityUtil.dip2px(getSupportedActivity(), 268) - NotchUtil.getNotchHeight(getSupportedActivity()));
        SYApplication.getInstance().setHost(list.get(position).courseType == 1 ? SYConstants.HTTP_URL_ONLINE : SYConstants.HTTP_URL_ACCOUNTANT);
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter.init(getSupportedActivity(), R.layout.item_course_menu, list);
        initView();
        loadData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && getSupportedActivity() != null) {
            StatusBarUtil.setTranslucentStatus(getSupportedActivity());
        }
    }

    /**
     * 初始化
     */
    private void initView() {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getDataBinding().titleIndicator.getLayoutParams();
        params.setMargins(0, NotchUtil.getNotchHeight(getSupportedActivity()), 0, 0);
        getDataBinding().titleIndicator.setLayoutParams(params);

        MagicIndicatorBuilder.MagicIndicatorConfiguration configuration = new MagicIndicatorBuilder.MagicIndicatorConfiguration(getSupportedActivity());
        configuration.labels = new String[]{"上元在线", "上元会计"};
        configuration.labelTextSize = 15;
        configuration.titleNormalColor = R.color.text_black;
        configuration.lineMode = LinePagerIndicator.MODE_EXACTLY;
        configuration.lineHeight = DensityUtil.dip2px(getSupportedActivity(), 4f);
        configuration.lineWidth = DensityUtil.dip2px(getSupportedActivity(), 20f);
        // init ViewPager
        getDataBinding().vpMain.setOffscreenPageLimit(1);
        getDataBinding().vpMain.setAdapter(new CommonViewPagerAdapter(getChildFragmentManager(), onlineCourseFragment, accountantCourseFragment));
        getDataBinding().vpMain.addOnPageChangeListener(new MagicIndicatorPageListener(getDataBinding().mainTabIndicator));
        // set MagicIndicator
        CommonNavigator commonNavigator = MagicIndicatorBuilder.buildCommonNavigator2(getSupportedActivity(), configuration, new MagicIndicatorBuilder.OnNavigatorClickListener2() {
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
                // 设置网络请求baseUrl，用于其他平台网络请求框架使用。
                SYApplication.getInstance().setHost(index == 0 ? SYConstants.HTTP_URL_ONLINE : SYConstants.HTTP_URL_ACCOUNTANT);
                if (index == 0) {
                    if (onlineCourseFragment.courseList.size() > 0) {
                        onlineCourseFragment.resetViewPagerHeight();
                    }
                } else {
                    if (accountantCourseFragment.courseList.size() > 0) {
                        accountantCourseFragment.resetViewPagerHeight();
                    }
                }
            }
        });
        getDataBinding().mainTabIndicator.setNavigator(commonNavigator);

        getDataBinding().vpMain.addOnPageChangeListener(new MagicIndicatorPageListener(getDataBinding().tabIndicator));
        // set MagicIndicator
        CommonNavigator commonNavigator2 = MagicIndicatorBuilder.buildCommonNavigator2(getSupportedActivity(), configuration, new MagicIndicatorBuilder.OnNavigatorClickListener2() {
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
                // 设置网络请求baseUrl，用于其他平台网络请求框架使用。
                SYApplication.getInstance().setHost(index == 0 ? SYConstants.HTTP_URL_ONLINE : SYConstants.HTTP_URL_ACCOUNTANT);
                if (index == 0) {
                    if (onlineCourseFragment.courseList.size() > 0) {
                        onlineCourseFragment.resetViewPagerHeight();
                    }
                } else {
                    if (accountantCourseFragment.courseList.size() > 0) {
                        accountantCourseFragment.resetViewPagerHeight();
                    }
                }
            }
        });
        getDataBinding().tabIndicator.setNavigator(commonNavigator2);

        // 设置scrollView滑动监听
        getDataBinding().scrollView.setOnScrollListener(scrollY -> {
            LogUtil.i("scrollY", "scrollView滑动距离：" + scrollY);
            if (scrollY > DensityUtil.dip2px(getSupportedActivity(), 210) - NotchUtil.getNotchHeight(getSupportedActivity())) {
                getDataBinding().titleIndicator.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getSupportedActivity().getWindow();
                    // 有些情况下需要先清除透明flag
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.bg_white));
                    StatusBarUtil.setCommonUI(getSupportedActivity());
                }
            } else {
                getDataBinding().titleIndicator.setVisibility(View.GONE);
                // 设置状态栏透明，沉浸式
                StatusBarUtil.setTranslucentStatus(getSupportedActivity());
            }
        });
    }

    /**
     * 开始Banner轮播
     */
    private void startBanner() {
        // 上元在线和上元会计banner均加载完毕
        if (isOnlineBannerLoaded && isAccountantBannerLoaded) {
            getDataBinding().banner
                    .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)// 设置banner样式，此处不显示指示器和标题
                    .setBannerAnimation(Transformer.Accordion) // 设置banner动画效果
                    .setImages(bannerUrls)    // 设置图片集合
//                .setBannerTitles(titles)                   // 设置标题集合（当banner样式有显示title时）
                    .setIndicatorGravity(BannerConfig.RIGHT)  // 设置指示器位置（当banner模式中有指示器时）
                    .setImageLoader(new ImageLoader() {          // 设置图片加载器
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                        @Override
                        public void displayImage(Context context, Object path, ImageView imageView) {
                            if (context != null && (context instanceof Activity && !((Activity) context).isDestroyed())) {
                                Glide.with(context).load((String) path).into(imageView);
                            }
                        }
                    })
                    .setOnBannerListener(position -> {        // banner点击事件

                    })
                    .start();                 // banner设置方法全部调用完毕时最后调用;
        }
    }

    /**
     * 加载数据
     */
    private void loadData() {
        SYDataTransport.create(SYConstants.ONLINE_BANNER, false)
                .isGET()
                .execute(new SYDataListener<String>() {

                    @Override
                    public void onSuccess(String data) {
                        isOnlineBannerLoaded = true;
                        List<Banner> banners = new Gson().fromJson(data, new TypeToken<List<Banner>>() {
                        }.getType());
                        for (Banner banner : banners) {
                            bannerUrls.add(banner.url);
                        }
                        // 开始轮播
                        startBanner();
                    }
                });

        SYDataTransport.create(SYConstants.ACCOUNTANT_BANNER, false)
                .isGET()
                .execute(new SYDataListener<String>() {

                    @Override
                    public void onSuccess(String data) {
                        isAccountantBannerLoaded = true;
                        List<Banner> banners = new Gson().fromJson(data, new TypeToken<List<Banner>>() {
                        }.getType());
                        for (Banner banner : banners) {
                            bannerUrls.add(banner.url);
                        }
                        // 开始轮播
                        startBanner();
                    }
                });
    }

    /**
     * 显示导航菜单
     */
    public View.OnClickListener onMenuShowClicked = v -> {
        isMenuShowed.set(true);
        getDataBinding().layout.setVisibility(View.VISIBLE);
        getDataBinding().bgLayout.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getSupportedActivity(), R.anim.slide_out_from_right);
        getDataBinding().layout.startAnimation(animation);
    };

    /**
     * 背景点击关闭菜单
     */
    public View.OnClickListener onBgClicked = v -> {
        Animation animation = AnimationUtils.loadAnimation(getSupportedActivity(), R.anim.slide_in_from_right);
        getDataBinding().layout.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getDataBinding().layout.setVisibility(View.GONE);
                getDataBinding().bgLayout.setVisibility(View.GONE);
                isMenuShowed.set(false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    };
}
