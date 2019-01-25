package com.edusoho.yunketang.ui.me.classroom;

import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.adapter.CommonViewPagerAdapter;
import com.edusoho.yunketang.base.MagicIndicatorBuilder;
import com.edusoho.yunketang.base.MagicIndicatorPageListener;
import com.edusoho.yunketang.base.NaviLifecycleActivity;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.databinding.ActivityClassDetailsBinding;
import com.edusoho.yunketang.edu.bean.Classroom;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.ui.common.ShareActivity;
import com.edusoho.yunketang.ui.login.LoginActivity;
import com.edusoho.yunketang.utils.DensityUtil;
import com.edusoho.yunketang.utils.JsonUtil;
import com.edusoho.yunketang.utils.RequestCodeUtil;
import com.edusoho.yunketang.utils.StringUtils;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

/**
 * Created by zy on 2018/11/9 0009.
 */
@Layout(value = R.layout.activity_class_details, rightButtonRes = R.drawable.icon_share)
public class ClassroomActivity extends NaviLifecycleActivity<ActivityClassDetailsBinding> {
    public static final int FROM_COURSE_CODE = RequestCodeUtil.next();
    public static final String COURSE_TYPE = "course_type";
    public static final String CLASSROOM_ID = "classroom_id";
    private int courseType; // 1、上元在线  2、上元会计
    public int classroomId;
    public Classroom classroom;

    public ObservableField<String> coverUrl = new ObservableField<>();
    private ClassBriefFragment classBriefFragment = new ClassBriefFragment();
    private ClassCatalogueFragment classCatalogueFragment = new ClassCatalogueFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseType = getIntent().getIntExtra(COURSE_TYPE, 0);
        classroomId = getIntent().getIntExtra(CLASSROOM_ID, 0);
        // 初始化
        initView();
        if (classroomId != 0 && courseType != 0) {
            // 加载数据
            loadData();
        } else {
            showSingleToast("课程不存在！");
        }
    }

    /**
     * 初始化
     */
    private void initView() {
        setTitleView("");
        MagicIndicatorBuilder.MagicIndicatorConfiguration configuration = new MagicIndicatorBuilder.MagicIndicatorConfiguration(this);
        configuration.labels = new String[]{"简介", "目录"};
        configuration.labelTextSize = 16;
        configuration.titleNormalColor = R.color.text_black;
        configuration.lineMode = LinePagerIndicator.MODE_EXACTLY;
        configuration.lineHeight = DensityUtil.dip2px(this, 2f);
        configuration.lineWidth = DensityUtil.dip2px(this, 50f);
        // init ViewPager
        getDataBinding().vpMain.setOffscreenPageLimit(1);
        getDataBinding().vpMain.setAdapter(new CommonViewPagerAdapter(getSupportFragmentManager(), classBriefFragment, classCatalogueFragment));
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

    /**
     * 加载数据
     */
    private void loadData() {
        SYDataTransport.create(String.format(courseType == 1 ? SYConstants.ONLINE_MY_CLASSROOM : SYConstants.ACCOUNTANT_MY_CLASSROOM, classroomId))
                .isGET()
                .execute(new SYDataListener<String>() {

                    @Override
                    public void onSuccess(String data) {
                        if (data.contains("errorMsg:Unauthorized")) {
                            showSingleToast("token过期，请重新登录!");
                            startActivity(LoginActivity.class);
                            return;
                        }
                        try {
                            // 将数据中富文本内容转成可被解析的json数据
                            String json = StringUtils.jsonStringConvert(StringUtils.replaceBlank(data));
                            classroom = JsonUtil.fromJson(json, Classroom.class);
                            refreshView();
                        } catch (Exception e) {

                        }
                    }
                });
    }

    /**
     * 刷新界面
     */
    private void refreshView() {
        coverUrl.set(classroom.cover.large);
        // 刷新简介
        classBriefFragment.refreshView(classroom);
        // 加载目录数据
        classCatalogueFragment.loadCatalogueData(courseType, classroom.id);
    }

    /**
     * 微信分享
     */
    @Override
    public void onRightButtonClick() {
        Intent intent = new Intent(this, ShareActivity.class);
        String shareText;
        if (!TextUtils.isEmpty(classroom.about)) {
            shareText = Html.fromHtml(classroom.about).toString();
        } else {
            shareText = "欢迎您的加入！";
        }
        intent.putExtra(ShareActivity.SHARE_URL, SYApplication.getInstance().host + "classroom/" + classroom.id);
        intent.putExtra(ShareActivity.SHARE_TITLE, classroom.title);
        intent.putExtra(ShareActivity.SHARE_CONTENT, shareText.length() > 20 ? shareText.substring(0, 20) : shareText);
        intent.putExtra(ShareActivity.SHARE_THUMB_URL, classroom.cover != null ? classroom.cover.small : "");
        startActivity(intent);
    }
}
