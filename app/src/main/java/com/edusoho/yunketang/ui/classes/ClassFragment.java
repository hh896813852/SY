package com.edusoho.yunketang.ui.classes;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableField;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edusoho.yunketang.base.BaseDialog;
import com.edusoho.yunketang.bean.MsgInfo;
import com.edusoho.yunketang.helper.AppPreferences;
import com.edusoho.yunketang.ui.MainTabActivity;
import com.edusoho.yunketang.utils.DateUtils;
import com.edusoho.yunketang.utils.DialogUtil;
import com.edusoho.yunketang.utils.NotchUtil;
import com.edusoho.yunketang.widget.dialog.TipDialog;
import com.google.gson.reflect.TypeToken;
import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.adapter.SYBaseAdapter;
import com.edusoho.yunketang.base.BaseFragment;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.ClassInfo;
import com.edusoho.yunketang.databinding.FragmentClassBinding;
import com.edusoho.yunketang.helper.ListViewHelper;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.ui.common.CaptureActivity;
import com.edusoho.yunketang.ui.login.LoginActivity;
import com.edusoho.yunketang.utils.DensityUtil;
import com.edusoho.yunketang.utils.LogUtil;
import com.edusoho.yunketang.utils.statusbar.StatusBarUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * @author huhao on 2018/7/4
 */
@Layout(value = R.layout.fragment_class)
public class ClassFragment extends BaseFragment<FragmentClassBinding> {
    public ObservableField<Boolean> isLogin = new ObservableField<>(false);
    public ObservableField<Boolean> hasClass = new ObservableField<>(false);
    public ObservableField<String> unReadCount = new ObservableField<>("0");

    private List<ClassInfo> list = new ArrayList<>();
    public SYBaseAdapter adapter = new SYBaseAdapter() {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            TextView classNameView = view.findViewById(R.id.classNameView);
            TextView classStatusView = view.findViewById(R.id.classStatusView);
            view.findViewById(R.id.itemView1).setVisibility(TextUtils.isEmpty(list.get(position).batch) ? View.VISIBLE : View.GONE);
            view.findViewById(R.id.itemView2).setVisibility(TextUtils.isEmpty(list.get(position).batch) ? View.GONE : View.VISIBLE);
            if (position <= lastChangeColorPosition) {
                classNameView.setTextColor(ContextCompat.getColor(getSupportedActivity(), R.color.text_white));
                classStatusView.setTextColor(ContextCompat.getColor(getSupportedActivity(), R.color.text_white));
            } else {
                classNameView.setTextColor(ContextCompat.getColor(getSupportedActivity(), R.color.text_black));
                classStatusView.setTextColor(ContextCompat.getColor(getSupportedActivity(), R.color.text_dark_gray));
            }
            // 查看课表
            view.findViewById(R.id.classScheduleView).setOnClickListener(v -> {
                Intent intent = new Intent(getSupportedActivity(), ClassScheduleActivity.class);
                intent.putExtra(ClassScheduleActivity.CLASS_ID, list.get(position).id);
                getSupportedActivity().startActivity(intent);
            });
            // 查看作业
            view.findViewById(R.id.taskView1).setOnClickListener(v -> {
                Intent intent = new Intent(getSupportedActivity(), CourseworkActivity.class);
                intent.putExtra(CourseworkActivity.CLASS_INFO, list.get(position));
                getSupportedActivity().startActivity(intent);
            });
            // 查看作业
            view.findViewById(R.id.taskView2).setOnClickListener(v -> {
                Intent intent = new Intent(getSupportedActivity(), CourseworkActivity.class);
                intent.putExtra(CourseworkActivity.CLASS_INFO, list.get(position));
                getSupportedActivity().startActivity(intent);
            });
            return view;
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CaptureActivity.REQUEST_FROM_CHECK_IN && resultCode == Activity.RESULT_OK) {
            String checkInfo = data.getStringExtra(CaptureActivity.EXTRAS_STRING_CHECK_IN);
            showCheckInDialog(checkInfo);
        }
    }

    /**
     * 显示考勤信息对话框
     */
    private void showCheckInDialog(String checkInfo) {
        DialogUtil.showTipDialog(getSupportedActivity(), "考勤状态", checkInfo);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter.init(getSupportedActivity(), R.layout.item_class, list);
        initView();
    }

    private int currentChangeColorPosition;
    private int lastChangeColorPosition;

    private void initView() {
        FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams) getDataBinding().titleLayout.getLayoutParams();
        params1.setMargins(0, NotchUtil.getNotchHeight(getSupportedActivity()), 0, 0);
        getDataBinding().titleLayout.setLayoutParams(params1);
        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) getDataBinding().titleLayout2.getLayoutParams();
        params2.setMargins(0, NotchUtil.getNotchHeight(getSupportedActivity()), 0, 0);
        getDataBinding().titleLayout2.setLayoutParams(params2);

        getDataBinding().scrollView.setOnScrollListener(scrollY -> {
            // 遍历list找出需要改变颜色的item的临界position
            for (int i = 0; i < list.size(); i++) {
                int itemH = list.get(i).itemHeight;
                if (itemH - scrollY > DensityUtil.dip2px(getSupportedActivity(), 90)) {
                    currentChangeColorPosition = i;
                    // 如果与上一个临界position不一致，则刷新listView
                    if (lastChangeColorPosition != currentChangeColorPosition) {
                        lastChangeColorPosition = currentChangeColorPosition;
                        adapter.notifyDataSetChanged();
                    }
                    break;
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        isLogin.set(SYApplication.getInstance().isLogin());
        if (isLogin.get()) {
            // 加载数据
            loadData();
            // 刷新消息
            refreshMsg();
        } else {
            hasClass.set(false);
            unReadCount.set("0");
            if (getActivity() != null) {
                ((MainTabActivity) getActivity()).unReadCount.set("0");
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && getSupportedActivity() != null) {
            if (hasClass.get()) {
                StatusBarUtil.setTranslucentStatus(getSupportedActivity());
            } else {
                StatusBarUtil.setImmersiveStatusBar(getSupportedActivity(), true);
            }
            if (isLogin.get()) {
                // 刷新消息
                refreshMsg();
            }
        }
    }

    /**
     * 加载数据
     */
    private void loadData() {
        SYDataTransport.create(SYConstants.MY_CLASS)
                .execute(new SYDataListener<List<ClassInfo>>() {

                    @Override
                    public void onSuccess(List<ClassInfo> data) {
                        list.clear();
                        list.addAll(data);
                        hasClass.set(data.size() > 0);
                        adapter.notifyDataSetChanged();
                        // 因为notifyDataSetChanged()方法是异步的，并且没有方法监听其什么结束
                        // 而我们需要界面绘制完成获取item高度，所以延迟300ms，进行假同步操作。
                        new Handler().postDelayed(() -> calculateItemHeight(), 300);
                    }
                }, new TypeToken<List<ClassInfo>>() {
                });
    }

    /**
     * 刷新消息
     */
    private void refreshMsg() {
        SYDataTransport.create(SYConstants.UNREAD_MSG_COUNT)
                .addParam("studentId", SYApplication.getInstance().getUser().syjyUser.id)
                .execute(new SYDataListener<MsgInfo>() {

                    @Override
                    public void onSuccess(MsgInfo data) {
                        if (SYApplication.getInstance().getUser() == null) {
                            return;
                        }
                        unReadCount.set(data.unReadCount);
                        if (getActivity() != null) {
                            ((MainTabActivity) getActivity()).unReadCount.set(data.unReadCount);
                        }
                        // 有消息
                        if (Integer.valueOf(data.unReadCount) > 0) {
                            // 获取之前保存的最新消息时间
                            long latestMsgTime = AppPreferences.getLatestMsgTime();
                            // 获取最新消息时间
                            long updateTime = data.time;
                            // 未保存过未读消息  或者  最新消息时间 > 之前保存的最新消息时间
                            if ((latestMsgTime == 0) || updateTime > latestMsgTime) {
                                // 保存系统最新消息时间
                                AppPreferences.setLatestMsgTime(updateTime);
                                // 如果允许声音提示
                                if (AppPreferences.getSettings().soundSwitchState == 1) {
                                    // 播放新消息提示音
                                    playTipTone();
                                }
                                // 如果允许震动提示
                                if (AppPreferences.getSettings().vibrationSwitchState == 1) {
                                    // 新消息震动提示
                                    playTipVibrator();
                                }
                            }
                        }
                    }
                }, MsgInfo.class);
    }

    /**
     * 新消息提示音
     */
    private void playTipTone() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone ringtone = RingtoneManager.getRingtone(getSupportedActivity(), notification);
        ringtone.play();
    }

    /**
     * 新消息震动提示
     */
    private void playTipVibrator() {
        Vibrator vibrator = (Vibrator) getSupportedActivity().getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(300);
        }
    }

    /**
     * 计算各个item高度，数据数量或item高度发生改变需重新计算
     */
    private void calculateItemHeight() {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).itemHeight = ListViewHelper.getPositionHeightOfListView(getDataBinding().listView, i);
        }
    }

    /**
     * 去登录
     */
    public View.OnClickListener onLoginClicked = v -> {
        startActivity(new Intent(getSupportedActivity(), LoginActivity.class));
    };

    /**
     * 我的消息
     */
    public View.OnClickListener onMsgClicked = v -> {
        startActivity(new Intent(getSupportedActivity(), MyMessageActivity.class));
    };

    /**
     * 扫一扫
     */
    public View.OnClickListener onScanClicked = v -> {
        // 权限检查
        permissionCheck();
    };

    /**
     * 权限检测申请
     */
    private void permissionCheck() {
        RxPermissions rxPermissions = new RxPermissions(getSupportedActivity());
        rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(grant -> {
                    if (grant) {
                        // 权限全部允许
                        startActivityForResult(new Intent(getSupportedActivity(), CaptureActivity.class), CaptureActivity.REQUEST_FROM_CHECK_IN);
                    } else {
                        // 至少有一个拒绝了
                        showSingleToast("未能取得相机或定位权限，无法扫码考勤！");
                    }
                });
    }
}
