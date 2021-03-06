package com.edusoho.yunketang.ui.course;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.adapter.CatalogueExpandableAdapter;
import com.edusoho.yunketang.base.BaseActivity;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.base.annotation.Translucent;
import com.edusoho.yunketang.bean.User;
import com.edusoho.yunketang.bean.lesson.LessonItem;
import com.edusoho.yunketang.databinding.ActivityCoursePlayerBinding;
import com.edusoho.yunketang.edu.api.CourseApi;
import com.edusoho.yunketang.edu.bean.CourseItem;
import com.edusoho.yunketang.edu.bean.CourseProject;
import com.edusoho.yunketang.edu.bean.CourseTask;
import com.edusoho.yunketang.edu.bean.TaskEvent;
import com.edusoho.yunketang.edu.http.HttpUtils;
import com.edusoho.yunketang.edu.http.SubscriberProcessor;
import com.edusoho.yunketang.helper.AppPreferences;
import com.edusoho.yunketang.helper.DialogHelper;
import com.edusoho.yunketang.helper.RegisterOtherPlatformHelper;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.ui.common.ShareActivity;
import com.edusoho.yunketang.ui.common.ValidateActivity;
import com.edusoho.yunketang.ui.login.LoginActivity;
import com.edusoho.yunketang.utils.DialogUtil;
import com.edusoho.yunketang.utils.JsonUtil;
import com.edusoho.yunketang.utils.NetworkUtils;
import com.edusoho.yunketang.utils.StringUtils;
import com.edusoho.yunketang.widget.SYVideoPlayer;
import com.edusoho.yunketang.widget.dialog.SimpleDialog;
import com.google.gson.reflect.TypeToken;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Translucent(value = false)
@Layout(value = R.layout.activity_course_player, rightButtonRes = R.drawable.icon_share_white)
public class CoursePlayerActivity extends BaseActivity<ActivityCoursePlayerBinding> {
    public static final String COURSE_PROJECT_ID = "course_project_id";
    public static final String COURSE_ID = "course_id";
    public static final String COURSE_PROJECT = "course_project";
    public static final String COURSE_COVER = "course_cover";
    public static final String COURSE_TYPE = "course_type";
    public static final String COURSE_TASK = "course_task";
    public static final String IS_MEMBER = "is_member";
    public static final String COURSE_CATALOGUE = "course_catalogue"; // 课程目录
    private int courseId;
    private int courseType; // 1、上元在线  2、上元会计

    private String courseCover;
    private String courseCatalogueJson;
    public CourseProject courseProject;
    private CourseTask currentTask;
    private List<CourseItem> expandableList = new ArrayList<>();
    private CatalogueExpandableAdapter expandableAdapter;

    private SYVideoPlayer videoPlayer;
    private OrientationUtils orientationUtils;
    private boolean isPlay;
    private boolean isPause;

    private TextView sendView; // dialog发送验证码按钮

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == LoginActivity.LOGIN_REQUEST_CODE) {
                finish();
            }
            if (requestCode == ValidateActivity.VALIDATE_CODE) {
                String dragCaptchaToken = data.getStringExtra("dragCaptchaToken");
                RegisterOtherPlatformHelper.setToken(dragCaptchaToken);
                sendView.performClick(); // 发送验证码点击
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseCover = getIntent().getStringExtra(COURSE_COVER);
        courseId = getIntent().getIntExtra(COURSE_ID, 0);
        courseType = getIntent().getIntExtra(COURSE_TYPE, 0);
        courseProject = (CourseProject) getIntent().getSerializableExtra(COURSE_PROJECT);
        currentTask = (CourseTask) getIntent().getSerializableExtra(COURSE_TASK);
        courseCatalogueJson = getIntent().getStringExtra(COURSE_CATALOGUE);
        setTitleView("");
        // 初始化ExpandListView
        initExpand();
        // 初始化目录
        initCatalogue();
        // 初始化播放器
        initPlayer();
        if (currentTask != null) {
            // 加载当前课程视频
            loadTaskData(currentTask.id);
        }
    }

    /**
     * 初始化ExpandListView
     */
    private void initExpand() {
        expandableAdapter = new CatalogueExpandableAdapter(this, expandableList, false, courseProject == null ? 1 : courseProject.tryLookable);
        getDataBinding().expandableView.setAdapter(expandableAdapter);
        expandableAdapter.setIsCourseMember(getIntent().getBooleanExtra(IS_MEMBER, false));
        expandableAdapter.setIsPlayActivity(true);
        // 去掉分割线
        getDataBinding().expandableView.setDivider(null);
        // 子项点击
        getDataBinding().expandableView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            currentTask = expandableList.get(groupPosition).childList.get(childPosition).task;
            loadTaskData(currentTask.id);
            for (CourseItem groupItem : expandableList) {
                if (groupItem.childList != null) {
                    for (CourseItem childItem : groupItem.childList) {
                        if (childItem.task != null) {
                            childItem.task.isSelected = currentTask.id == childItem.task.id;
                        }
                    }
                }
            }
            expandableAdapter.notifyDataSetChanged();




            return false;
        });
    }

    /**
     * 初始化目录
     */
    private void initCatalogue() {
        try {
            // 将数据中富文本内容转成可被解析的json数据
            String json = StringUtils.jsonStringConvert(StringUtils.replaceBlank(courseCatalogueJson));
            expandableList.addAll(JsonUtil.fromJson(json, new TypeToken<List<CourseItem>>() {
            }));
            expandableAdapter.notifyDataSetChanged();
            // 默认全部展开
            for (int i = 0; i < expandableList.size(); i++) {
                getDataBinding().expandableView.expandGroup(i);
            }
        } catch (Exception e) {

        }
    }

    /**
     * 初始化播放器
     */
    private void initPlayer() {
        videoPlayer = getDataBinding().videoPlayer;
        // 外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, videoPlayer);
        // 初始化不打开外部的旋转
        orientationUtils.setEnable(false);
        // 设置封面
        ImageView thumbImage = new ImageView(this);
        thumbImage.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(this).load(courseCover).into(thumbImage);

        GSYVideoOptionBuilder gsyVideoOption = new GSYVideoOptionBuilder();
        gsyVideoOption.setThumbImageView(thumbImage) // 封面
                .setIsTouchWiget(true)  // 是否可以滑动界面改变进度，声音等
                .setRotateViewAuto(false)// 是否开启自动旋转
                .setLockLand(false)      // 一全屏就锁屏
                .setAutoFullWithSize(true)// 自动全屏
                .setShowFullAnimation(true)// 显示全屏动画
                .setNeedLockFull(true) // 全屏有锁定屏幕功能
                .setCacheWithPlay(true) // 边缓存边播放
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                        //开始播放了才能旋转和全屏
                        orientationUtils.setEnable(true);
                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        if (orientationUtils != null) {
                            orientationUtils.backToProtVideo();
                        }
                    }

                    @Override
                    public void onAutoComplete(String url, Object... objects) {
                        super.onAutoComplete(url, objects);
                        // 播放完重置进度和时间
                        ((SYVideoPlayer) objects[1]).resetProgressAndTime();
                        // 记录播放完成
                        onRecord(true, currentTask.id);
                    }
                })
                .setLockClickListener((view, lock) -> { // 锁屏点击
                    if (orientationUtils != null) {
                        //配合下方的onConfigurationChanged
                        orientationUtils.setEnable(!lock);
                    }
                }).build(videoPlayer);
        // 全屏按钮点击
        videoPlayer.getFullscreenButton().setOnClickListener(v -> {
            // 直接横屏
            orientationUtils.resolveByClick();
            // 第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
            videoPlayer.startWindowFullscreen(CoursePlayerActivity.this, true, true);
        });
        // 获取返回按键，设置其隐藏
        videoPlayer.getBackButton().setVisibility(View.GONE);
    }

    /**
     * 加载当前课程视频
     */
    private void loadTaskData(int taskId) {
        // 获取该课程信息
        SYDataTransport.create(String.format(courseType == 1 ? SYConstants.ONLINE_GET_LESSON : SYConstants.ACCOUNTANT_GET_LESSON, taskId))
                .isGET2()
                .execute(new SYDataListener<String>() {

                    @Override
                    public void onSuccess(String data) {
                        try {
                            // 将数据中富文本内容转成可被解析的json数据
                            String json = StringUtils.jsonStringConvert(data);
                            LessonItem lessonItem = JsonUtil.fromJson(json, LessonItem.class);
                         //   lessonItem.mediaUri = "http://edusoho.gensee.com/webcast/site/entry/join-065df0ae6cc44863a6fb80f64b810f41?token=260806&nickName=%E9%99%86%E5%BA%86%E8%B6%85&uid=1000013525&k=1552284503910e84f8ed588a31756072";
                            if (lessonItem != null && !TextUtils.isEmpty(lessonItem.mediaUri)) {
                                String lessonUrl = lessonItem.type.equals("video") ? lessonItem.mediaUri : lessonItem.audioUri;
                                canPlay(filterUrl(lessonUrl));
                            } else {
                                showSingleToast("视频不存在！");
                            }
                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onFail(int status, String failMessage) {
                        super.onFail(status, failMessage);
                    }
                });
    }

    /**
     * 能否播放
     */
    private void canPlay(String lessonUrl) {
        if (AppPreferences.getSettings().isAllow4GPlay == 1 || NetworkUtils.isWifiConnected(this)) {
            if (!isPause) {
                play(lessonUrl);
            }
        } else {
            DialogUtil.showSimpleAnimDialog(this, "您正在使用移动网络，是否继续观看？", "暂时不看", "继续观看", new SimpleDialog.OnSimpleClickListener() {
                @Override
                public void OnLeftBtnClicked(SimpleDialog dialog) {
                    dialog.dismiss();
                }

                @Override
                public void OnRightBtnClicked(SimpleDialog dialog) {
                    dialog.dismiss();
                    play(lessonUrl);
                }
            });
        }
    }

    /**
     * 播放
     */
    private void play(String lessonUrl) {
        isPlay = true;
        videoPlayer.setUp(lessonUrl, true, "");
        // 播放
        videoPlayer.startPlayLogic();
        // 记录播放中
        onRecord(false, currentTask.id);
    }

    @Override
    public void onBackPressed() {
        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        videoPlayer.getCurrentPlayer().onVideoPause();
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        videoPlayer.getCurrentPlayer().onVideoResume(false);
        super.onResume();
        isPause = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoPlayer != null) {
            videoPlayer.release();
        }
        if (orientationUtils != null) {
            orientationUtils.releaseListener();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            videoPlayer.onConfigurationChanged(this, newConfig, orientationUtils, true, true);
        }
    }

    /**
     * 修正音视频URL
     */
    private String filterUrl(String url) {
        if (url.contains("?")) {
            String[] urls = url.split("\\?");
            if (urls.length > 1) {
                return urls[0];
            }
        }
        return url;
    }

    /**
     * 记录学习状态，如果已经Finish，自动忽略-
     *
     * @param isFinish 是否完成
     */
    private void onRecord(boolean isFinish, int taskId) {
        if (currentTask == null) {
            return;
        }
        if (currentTask.isFinish()) {
            return;
        }
        getTaskResult(isFinish)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SubscriberProcessor<TaskEvent>() {
                    @Override
                    public void onNext(TaskEvent taskEvent) {
                        for (CourseItem groupItem : expandableList) {
                            if (groupItem.childList != null) {
                                for (CourseItem childItem : groupItem.childList) {
                                    if (childItem.task != null && taskId == childItem.task.id) {
                                        childItem.task.result = taskEvent.result;
                                    }
                                }
                            }
                        }
                    }
                });
    }

    private Map<String, String> mFieldMaps = new HashMap<>();

    private Observable<TaskEvent> getTaskResult(boolean isFinish) {
        if (isFinish) {
            return HttpUtils.getInstance()
                    .addTokenHeader(SYApplication.getInstance().token)
                    .createApi(CourseApi.class)
                    .setCourseTaskFinish(courseProject.id, currentTask.id);
        } else {
            mFieldMaps.put("lastTime", "");
            return HttpUtils.getInstance()
                    .addTokenHeader(SYApplication.getInstance().token)
                    .createApi(CourseApi.class)
                    .setCourseTaskDoing(courseProject.id, currentTask.id, mFieldMaps);
        }
    }

    /**
     * 评价点击
     */
    public void onEvaluateClick(View view) {
        User loginUser = SYApplication.getInstance().getUser();
        if (loginUser == null) {
            showSingleToast("请先登录");
            // 去登陆
            toLoginActivity();
        } else {
            if (SYApplication.getInstance().hasTokenInOtherPlatform(courseType)) {
                if (SYApplication.getInstance().isRegisterInOtherPlatform(courseType)) { // 注册了
                    // 去登录
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra(LoginActivity.LOGIN_PLATFORM, courseType + 1);
                    startActivity(intent);
                } else { // 未注册
                    // 去验证注册
                    DialogHelper.showHelpRegisterDialog(this, courseType, new DialogHelper.OnRegisterOtherPlatformListener() {
                        @Override
                        public void registerSuccess() {
                            // 去评价Activity
                            toEvaluateActivity();
                        }

                        @Override
                        public void onSMSError(TextView sendCodeView) {
                            sendView = sendCodeView;
                            // 去验证图片Activity
                            toValidateActivity();
                        }
                    });
                }
            } else {
                // 去评价Activity
                toEvaluateActivity();
            }
        }
    }

    /**
     * 微信分享
     */
    @Override
    public void onRightButtonClick() {
        Intent intent = new Intent(this, ShareActivity.class);
        intent.putExtra(ShareActivity.SHARE_URL, SYApplication.getInstance().host + "course_set/" + courseProject.courseSet.id);
        intent.putExtra(ShareActivity.SHARE_TITLE, courseProject.courseSet.title);
        intent.putExtra(ShareActivity.SHARE_CONTENT, courseProject.courseSet.summary);
        intent.putExtra(ShareActivity.SHARE_THUMB_URL, courseProject.courseSet.cover.small);
        startActivity(intent);
    }

    /**
     * 去登录
     */
    private void toLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LoginActivity.LOGIN_REQUEST_CODE);
    }

    /**
     * 去评价Activity
     */
    private void toEvaluateActivity() {
        Intent intent = new Intent(CoursePlayerActivity.this, CourseEvaluateActivity.class);
        intent.putExtra(CourseEvaluateActivity.COURSE_PROJECT_ID, courseProject.id);
        intent.putExtra(CourseEvaluateActivity.COURSE_TYPE, courseType);
        startActivity(intent);
    }

    /**
     * 去验证图片Activity
     */
    private void toValidateActivity() {
        Intent intent = new Intent(CoursePlayerActivity.this, ValidateActivity.class);
        intent.putExtra(ValidateActivity.COURSE_TYPE, courseType);
        startActivityForResult(intent, ValidateActivity.VALIDATE_CODE);
    }
}
