package com.edusoho.yunketang.ui.course;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.edusoho.yunketang.ui.common.ShareActivity;
import com.google.gson.reflect.TypeToken;
import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.adapter.CommonViewPagerAdapter;
import com.edusoho.yunketang.base.MagicIndicatorBuilder;
import com.edusoho.yunketang.base.MagicIndicatorPageListener;
import com.edusoho.yunketang.base.NaviLifecycleActivity;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.User;
import com.edusoho.yunketang.bean.VipLevel;
import com.edusoho.yunketang.bean.seting.VIPSetting;
import com.edusoho.yunketang.databinding.ActivityCourseDetailsBinding;
import com.edusoho.yunketang.edu.api.CourseApi;
import com.edusoho.yunketang.edu.api.PluginsApi;
import com.edusoho.yunketang.edu.bean.CourseMember;
import com.edusoho.yunketang.edu.bean.CourseProject;
import com.edusoho.yunketang.edu.bean.CourseSet;
import com.edusoho.yunketang.edu.bean.ErrorResult;
import com.edusoho.yunketang.edu.http.HttpUtils;
import com.edusoho.yunketang.edu.http.SubscriberProcessor;
import com.edusoho.yunketang.edu.order.confirm.ConfirmOrderActivity;
import com.edusoho.yunketang.edu.utils.SettingHelper;
import com.edusoho.yunketang.edu.utils.SharedPreferencesHelper;
import com.edusoho.yunketang.helper.CourseAccessHelper;
import com.edusoho.yunketang.helper.DialogHelper;
import com.edusoho.yunketang.helper.RegisterOtherPlatformHelper;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.ui.common.ValidateActivity;
import com.edusoho.yunketang.ui.login.LoginActivity;
import com.edusoho.yunketang.utils.DensityUtil;
import com.edusoho.yunketang.utils.HttpUtil;
import com.edusoho.yunketang.utils.JsonUtil;
import com.edusoho.yunketang.utils.ProgressDialogUtil;
import com.edusoho.yunketang.utils.StringUtils;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.navi.NaviLifecycle;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zy on 2018/11/9 0009.
 */
@Layout(value = R.layout.activity_course_details)
public class CourseDetailsActivity extends NaviLifecycleActivity<ActivityCourseDetailsBinding> {
    public static final String COURSE_TYPE = "course_type";
    public static final String COURSE_ID = "course_id";
    private int courseType; // 1、上元在线  2、上元会计
    public int courseId;
    public List<CourseProject> courseList;
    public CourseProject courseProject;
    private CourseMember courseMember;
    private List<VipLevel> mVipInfos;
    private TextView sendView; // dialog发送验证码按钮

    public ObservableField<String> coverUrl = new ObservableField<>();
    public ObservableField<Boolean> isCollected = new ObservableField<>(false);
    public ObservableField<Boolean> isJoined = new ObservableField<>(false);
    private BriefIntroFragment briefIntroFragment = new BriefIntroFragment();
    private CatalogueFragment catalogueFragment = new CatalogueFragment();
    private EvaluateFragment evaluateFragment = new EvaluateFragment();

    private final LifecycleProvider<ActivityEvent> mActivityLifeProvider = NaviLifecycle.createActivityLifecycleProvider(this);

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ValidateActivity.VALIDATE_CODE && resultCode == Activity.RESULT_OK) {
            String dragCaptchaToken = data.getStringExtra("dragCaptchaToken");
            RegisterOtherPlatformHelper.setToken(dragCaptchaToken);
            sendView.performClick(); // 发送验证码点击
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseType = getIntent().getIntExtra(COURSE_TYPE, 0);
        courseId = getIntent().getIntExtra(COURSE_ID, 0);
        // 初始化
        initView();
        // 获得VIP
        getVIPLevels();
        if (courseId != 0 && courseType != 0) {
            // 加载数据
            loadData();
        } else {
            showSingleToast("课程不存在！");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 是否收藏
        loadIsCollect();
        if (courseProject != null && courseProject.id != 0) {
            // 是否加入了学习
            loadIsJoin();
        }
    }

    /**
     * 初始化
     */
    private void initView() {
        MagicIndicatorBuilder.MagicIndicatorConfiguration configuration = new MagicIndicatorBuilder.MagicIndicatorConfiguration(this);
        configuration.labels = new String[]{"简介", "目录", "评价"};
        configuration.labelTextSize = 16;
        configuration.titleNormalColor = R.color.text_black;
        configuration.lineMode = LinePagerIndicator.MODE_EXACTLY;
        configuration.lineHeight = DensityUtil.dip2px(this, 2f);
        configuration.lineWidth = DensityUtil.dip2px(this, 50f);
        // init ViewPager
        getDataBinding().vpMain.setOffscreenPageLimit(1);
        getDataBinding().vpMain.setAdapter(new CommonViewPagerAdapter(getSupportFragmentManager(), briefIntroFragment, catalogueFragment, evaluateFragment));
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
        SYDataTransport.create(String.format(courseType == 1 ? SYConstants.ONLINE_COURSES : SYConstants.ACCOUNTANT_COURSES, courseId))
                .isGET()
                .execute(new SYDataListener<String>() {

                    @Override
                    public void onSuccess(String data) {
                        if (data.contains("errorMsg:Unauthorized")) {
                            showSingleToast("token过期，请重新登录!");
                            startActivity(LoginActivity.class);
                            return;
                        }
                        // 将数据中富文本内容转成可被解析的json数据
                        String json = StringUtils.jsonStringConvert(StringUtils.replaceBlank(data));
                        courseList = JsonUtil.fromJson(json, new TypeToken<List<CourseProject>>() {
                        });
                        if (courseList != null && courseList.size() > 0 && courseList.get(0) != null) {
                            courseProject = courseList.get(0);
                            refreshView();
                            loadIsJoin();
                        }
                    }
                });
    }

    /**
     * 是否收藏
     */
    private void loadIsCollect() {
        User loginUser = SYApplication.getInstance().getUser();
        if (loginUser == null) {
            return;
        }
        if (!SYApplication.getInstance().hasTokenInOtherPlatform(courseType)) {
            SYDataTransport.create(String.format(courseType == 1 ? SYConstants.ONLINE_IS_COLLECT : SYConstants.ACCOUNTANT_IS_COLLECT, courseId))
                    .isGET()
                    .directReturn()
                    .execute(new SYDataListener<String>() {

                        @Override
                        public void onSuccess(String data) {
                            try {
                                JSONObject jsonObject = new JSONObject(data);
                                if (jsonObject.has("isFavorite")) {
                                    isCollected.set(jsonObject.getBoolean("isFavorite"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    /**
     * 是否加入了学习
     */
    private void loadIsJoin() {
        User loginUser = SYApplication.getInstance().getUser();
        if (loginUser == null) {
            return;
        }
        if (!SYApplication.getInstance().hasTokenInOtherPlatform(courseType)) {
            SYDataTransport.create(String.format(courseType == 1 ? SYConstants.ONLINE_COURSE_MEMBER : SYConstants.ACCOUNTANT_COURSE_MEMBER, courseProject.id))
                    .isGET()
                    .directReturn()
                    .execute(new SYDataListener<String>() {

                        @Override
                        public void onSuccess(String data) {
                            if (data.length() > 100) { // 简单判断下成功返回了...
                                courseMember = JsonUtil.fromJson(data, CourseMember.class);
                            }
                            catalogueFragment.setIsCourseMember(courseMember != null);
                            isJoined.set(courseMember != null);
                        }
                    });
        }
    }

    /**
     * 刷新界面
     */
    private void refreshView() {
        coverUrl.set(courseProject.courseSet.cover.large);
        // 刷新简介
        briefIntroFragment.refreshView(courseProject);
        // 加载目录数据
        catalogueFragment.loadCatalogueData(courseType, courseProject.id);
        // 加载评论数据
        evaluateFragment.loadEvaluateData(courseType, courseId);
    }

    /**
     * 微信分享
     */
    public void onShareClick(View view) {
        Intent intent = new Intent(this, ShareActivity.class);
        intent.putExtra(ShareActivity.SHARE_URL, SYApplication.getInstance().host + "course_set/" + courseProject.courseSet.id);
        intent.putExtra(ShareActivity.SHARE_TITLE, courseProject.courseSet.title);
        intent.putExtra(ShareActivity.SHARE_CONTENT, courseProject.courseSet.summary);
        intent.putExtra(ShareActivity.SHARE_THUMB_URL, courseProject.courseSet.cover.small);
        startActivity(intent);
    }

    /**
     * 加入学习
     */
    public void onJoinStudyClick(View view) {
        User loginUser = SYApplication.getInstance().getUser();
        if (loginUser == null) { // 未登录
            startActivity(LoginActivity.class);
        } else { // 已登录
            if (SYApplication.getInstance().hasTokenInOtherPlatform(courseType)) { // 没有toke
                if (SYApplication.getInstance().isRegisterInOtherPlatform(courseType)) { // 注册了
                    // 去登录
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra(LoginActivity.LOGIN_PLATFORM, courseType + 1);
                    startActivity(intent);
                } else { // 未注册
                    // 去验证注册
                    checkHelpRegister();
                }
            } else {
                if (isJoined.get()) { // 去学习
                    Intent intent = new Intent(this, CoursePlayerActivity.class);
                    intent.putExtra(CoursePlayerActivity.COURSE_PROJECT, courseProject);
                    intent.putExtra(CoursePlayerActivity.COURSE_COVER, coverUrl.get());
                    intent.putExtra(CoursePlayerActivity.COURSE_TYPE, courseType);
                    intent.putExtra(CoursePlayerActivity.COURSE_ID, courseId);
                    intent.putExtra(CoursePlayerActivity.COURSE_CATALOGUE, JsonUtil.toJson(catalogueFragment.expandableList));
                    startActivity(intent);
                } else { // 加入学习
                    if (courseList != null) {
                        if (courseList.size() == 1) {
                            CourseProject courseProject = courseList.get(0);
                            if (CourseAccessHelper.COURSE_SUCCESS.equals(courseProject.access.code)) {
                                joinFreeOrVipCourse(loginUser);
                            } else if (CourseAccessHelper.ONLY_VIP_JOIN_WAY.equals(courseProject.access.code)) {
                                if (mVipInfos != null) {
                                    for (VipLevel vipInfo : mVipInfos) {
                                        if (vipInfo.id == courseProject.vipLevelId) {
                                            showSingleToast(String.format(getString(CourseAccessHelper.getErrorResId(courseProject.access.code)), vipInfo.name));
                                        }
                                    }
                                }
                            } else {
                                showSingleToast(getString(CourseAccessHelper.getErrorResId(courseProject.access.code)));
                            }
                        } else {
//                        showPlanDialog(courseList, mVipInfos, mCourseSet);
                        }
                    }
                }
            }
        }
    }

    /**
     * 获得vip等级
     */
    private void getVIPLevels() {
        VIPSetting vipSetting = SettingHelper.getSetting(VIPSetting.class, this, SharedPreferencesHelper.SchoolSetting.VIP_SETTING);
        if (vipSetting != null && vipSetting.isEnabled()) {
            HttpUtils.getInstance()
                    .createApi(PluginsApi.class)
                    .getVIPLevels()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(mActivityLifeProvider.<List<VipLevel>>bindToLifecycle())
                    .subscribe(new SubscriberProcessor<List<VipLevel>>() {

                        @Override
                        public void onError(ErrorResult.Error error) {
                            showSingleToast(error.message);
                        }

                        @Override
                        public void onNext(List<VipLevel> vipLevels) {
                            mVipInfos = vipLevels;
                        }
                    });
        }
    }

    /**
     * 点击收藏
     */
    public void onCollectClick(View view) {
        User loginUser = SYApplication.getInstance().getUser();
        if (loginUser == null) { // 未登录
            startActivity(LoginActivity.class);
        } else { // 已登录
            if (SYApplication.getInstance().hasTokenInOtherPlatform(courseType)) {
                if (SYApplication.getInstance().isRegisterInOtherPlatform(courseType)) { // 注册了
                    // 去登录
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra(LoginActivity.LOGIN_PLATFORM, courseType + 1);
                    startActivity(intent);
                } else { // 未注册
                    // 去验证注册
                    checkHelpRegister();
                }
            } else {
                // 收藏与否
                setCollectStatus(courseType == 1 ? loginUser.syzxToken : loginUser.sykjToken);
            }
        }
    }

    /**
     * 设置收藏状态
     */
    private void setCollectStatus(String token) {
        if (isCollected.get()) { // 已收藏，取消收藏
            HttpUtil.doDelete(String.format(courseType == 1 ? SYConstants.ONLINE_COLLECT_CANCEL : SYConstants.ACCOUNTANT_COLLECT_CANCEL, courseId), token)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable throwable) {
                        }

                        @Override
                        public void onNext(String data) {
                            try {
                                JSONObject jsonObject = new JSONObject(data);
                                if (jsonObject.has("success")) {
                                    isCollected.set(false);
                                    showSingleToast("已取消收藏");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else { // 未收藏，去收藏
            SYDataTransport.create(courseType == 1 ? SYConstants.ONLINE_COLLECT : SYConstants.ACCOUNTANT_COLLECT)
                    .isJsonPost(false)
                    .directReturn()
                    .addHead("Accept", "application/vnd.edusoho.v2+json")
                    .addParam("courseSetId", courseId)
                    .execute(new SYDataListener<String>() {

                        @Override
                        public void onSuccess(String data) {
                            try {
                                JSONObject jsonObject = new JSONObject(data);
                                if (jsonObject.has("success")) {
                                    isCollected.set(true);
                                    showSingleToast("收藏成功！");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    /**
     * 验证身份，隐藏注册
     */
    private void checkHelpRegister() {
        DialogHelper.showHelpRegisterDialog(this, courseType, new DialogHelper.OnRegisterOtherPlatformListener() {
            @Override
            public void registerSuccess() {
                showSingleToast("身份验证成功！");
            }

            @Override
            public void onSMSError(TextView sendCodeView) {
                sendView = sendCodeView;
                Intent intent = new Intent(CourseDetailsActivity.this, ValidateActivity.class);
                intent.putExtra(ValidateActivity.COURSE_TYPE, courseType);
                startActivityForResult(intent, ValidateActivity.VALIDATE_CODE);
            }
        });
    }

    private void joinFreeOrVipCourse(User loginUser) {
        ProgressDialogUtil.showProgress(this, "正在加入中...");
        HttpUtils.getInstance()
                .addTokenHeader(courseType == 1 ? loginUser.syzxToken : loginUser.sykjToken)
                .createApi(CourseApi.class)
                .joinFreeOrVipCourse(courseProject.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mActivityLifeProvider.<CourseMember>bindToLifecycle())
                .subscribe(new SubscriberProcessor<CourseMember>() {
                    @Override
                    public void onError(String e) {
                        ProgressDialogUtil.hideProgress();
                    }

                    @Override
                    public void onNext(CourseMember courseMember) {
                        ProgressDialogUtil.hideProgress();
                        if (courseMember != null && courseMember.user != null) {
                            showToast("加入成功！");
                            isJoined.set(true);
                        } else {
                            if (courseProject != null) {
                                ConfirmOrderActivity.launch(CourseDetailsActivity.this, courseProject.courseSet.id, courseProject.id);
                            }
                        }
                    }
                });
    }

    /**
     * 显示学习计划dialog
     */
    public void showPlanDialog(List<CourseProject> courseProjects, List<VipLevel> vipInfo, CourseSet courseSet) {
//        if (mSelectDialog == null) {
//            mSelectDialog = new SelectProjectDialog();
//            mSelectDialog.setData(courseProjects, vipInfo);
//        }
//        if (mCourseProjects != null) {
//            mSelectDialog.reFreshData(mCourseProjects);
//        }
//        mSelectDialog.setCourseIndex(mCourseIndex);
//        mSelectDialog.show(getSupportFragmentManager(), "SelectProjectDialog");
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }
}
