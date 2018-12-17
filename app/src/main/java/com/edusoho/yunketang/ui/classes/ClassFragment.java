package com.edusoho.yunketang.ui.classes;

import android.Manifest;
import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

/**
 * @author huhao on 2018/7/4
 */
@Layout(value = R.layout.fragment_class)
public class ClassFragment extends BaseFragment<FragmentClassBinding> {
    public ObservableField<Boolean> isLogin = new ObservableField<>(false);
    public ObservableField<Boolean> hasClass = new ObservableField<>(false);

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
                getSupportedActivity().startActivity(intent);
            });
            // 查看作业
            view.findViewById(R.id.taskView2).setOnClickListener(v -> {
                Intent intent = new Intent(getSupportedActivity(), CourseworkActivity.class);
                getSupportedActivity().startActivity(intent);
            });
            return view;
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter.init(getSupportedActivity(), R.layout.item_class, list);
        initView();
    }

    private int currentChangeColorPosition;
    private int lastChangeColorPosition;

    private void initView() {
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
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && getSupportedActivity() != null) {
            StatusBarUtil.setTranslucentStatus(getSupportedActivity());
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
     * 扫一扫
     */
    public View.OnClickListener onScanClicked = v -> {
        permissionCheck(); // 摄像头权限检查
    };

    /**
     * 权限检测申请
     */
    private void permissionCheck() {
        RxPermissions rxPermissions = new RxPermissions(getSupportedActivity());
        rxPermissions.requestEach(Manifest.permission.CAMERA)
                .subscribe(permission -> {
                    if (permission.granted) {
                        // 权限允许
                        startActivity(new Intent(getSupportedActivity(), CaptureActivity.class));
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // 权限拒绝，等待下次询问
                        LogUtil.i("RxPermissions", "权限拒绝，等待下次询问：" + permission.name);
                    } else {
                        // 拒绝权限，不再弹出询问框，请前往APP应用设置打开此权限
                        LogUtil.i("RxPermissions", "拒绝权限，不再弹出询问框：" + permission.name);
                    }
                });
    }
}
