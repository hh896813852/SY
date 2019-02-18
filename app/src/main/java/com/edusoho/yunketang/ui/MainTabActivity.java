package com.edusoho.yunketang.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.adapter.CommonViewPagerAdapter;
import com.edusoho.yunketang.base.BaseActivity;
import com.edusoho.yunketang.base.BaseFragment;
import com.edusoho.yunketang.base.MagicIndicatorBuilder;
import com.edusoho.yunketang.base.MagicIndicatorPageListener;
import com.edusoho.yunketang.base.MainTabItem;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.base.annotation.Translucent;
import com.edusoho.yunketang.base.core.ActivityManager;
import com.edusoho.yunketang.databinding.ActivityMainTabBinding;
import com.edusoho.yunketang.helper.UpdateHelper;
import com.edusoho.yunketang.ui.classes.ClassFragment;
import com.edusoho.yunketang.ui.course.CourseFragment;
import com.edusoho.yunketang.ui.me.PersonalFragment;
import com.edusoho.yunketang.ui.testlib.TestLibFragment;
import com.edusoho.yunketang.utils.DensityUtil;
import com.edusoho.yunketang.utils.LogUtil;
import com.edusoho.yunketang.utils.ScreenUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;

@Translucent
@Layout(value = R.layout.activity_main_tab)
public class MainTabActivity extends BaseActivity<ActivityMainTabBinding> {
    private ArrayList<MainTabItem> mainTabItems = new ArrayList<>();
    private ArrayList<BaseFragment> fragments = new ArrayList<>();

    public CourseFragment courseFragment = new CourseFragment();
    private ClassFragment classFragment = new ClassFragment();
    private TestLibFragment testLibFragment = new TestLibFragment();
    private PersonalFragment personalFragment = new PersonalFragment();

    public ObservableField<String> unReadCount = new ObservableField<>("0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 清除该Activity下面的所有Activity
        ActivityManager.clearBottomActivitiesUntil(this);
        // 权限检测申请
        permissionCheck();
        // 初始化数据
        initData();
        // 初始化
        initView();
    }

    private void initData() {
        mainTabItems.add(new MainTabItem("课程", R.drawable.icon_tab_course_selected, R.drawable.icon_tab_course_normal));
        mainTabItems.add(new MainTabItem("班级", R.drawable.icon_tab_class_selected, R.drawable.icon_tab_class_normal));
        mainTabItems.add(new MainTabItem("题库", R.drawable.icon_tab_test_lib_selected, R.drawable.icon_tab_test_lib_normal));
        mainTabItems.add(new MainTabItem("我的", R.drawable.icon_tab_my_selected, R.drawable.icon_tab_my_normal));

        fragments.add(courseFragment);
        fragments.add(classFragment);
        fragments.add(testLibFragment);
        fragments.add(personalFragment);
    }

    /**
     * 初始化
     */
    private void initView() {
        // init ViewPager
        getDataBinding().vpMain.setOffscreenPageLimit(3);
        getDataBinding().vpMain.setAdapter(new CommonViewPagerAdapter(getSupportFragmentManager(), fragments));
        getDataBinding().vpMain.addOnPageChangeListener(new MagicIndicatorPageListener(getDataBinding().mainTabIndicator));
        // set MagicIndicator
        CommonNavigator commonNavigator = MagicIndicatorBuilder.buildCommonBottomTabNavigator(this, mainTabItems.toArray(new MainTabItem[mainTabItems.size()]), new MagicIndicatorBuilder.OnNavigatorClickListener() {
            @Override
            public void onNavigatorClickListener(int index) {
                MainTabActivity.this.getDataBinding().vpMain.setCurrentItem(index, false);
            }
        });
        getDataBinding().mainTabIndicator.setNavigator(commonNavigator);

        int screenW = ScreenUtil.getScreenWidth(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(screenW * 25 / 64, DensityUtil.dip2px(this, 2), 0, 0);
        getDataBinding().unreadCountView.setLayoutParams(params);
    }

    /**
     * 检测更新
     */
    private void checkUpdate() {
        UpdateHelper.checkUpdateIndex(this);
    }

    /**
     * 权限检测申请
     */
    private void permissionCheck() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEach(Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(permission -> {
                    if (permission.granted) {
                        // 权限允许
                        LogUtil.i("RxPermissions", "允许：" + permission.name);
                        if (permission.name.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            // 检测更新
                            checkUpdate();
                        }
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // 权限拒绝，等待下次询问
                        LogUtil.i("RxPermissions", "权限拒绝，等待下次询问：" + permission.name);
                    } else {
                        // 拒绝权限，不再弹出询问框，请前往APP应用设置打开此权限
                        LogUtil.i("RxPermissions", "拒绝权限，不再弹出询问框：" + permission.name);
                    }
                });
    }

    private long pressBackButtonTime = 0;

    @Override
    public void onBackPressed() {
        // 两秒内点击两次返回键
        if ((System.currentTimeMillis() - pressBackButtonTime) > 2000) {
            showToast("再按一次退出程序");
            pressBackButtonTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    /**
     * 某种原因系统回收MianActivity
     * ——FragmentA被保存状态未被回收
     * ——再次点击app进入——首先加载的是未被回收的FragmentA的页面
     * ——由于MainActivity被回收，系统会重启MainActivity，FragmentA也会被再次加载
     * ——页面出现混乱，因为一层未回收的FragmentA覆盖在其上面
     * ——（假如FragmentA使用到了getActivity()方法）会报空指针，出现crash
     * <p>
     * 解决办法，注释onSaveInstanceState 的 super.onSaveInstanceState(outState);
     */
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }
}
