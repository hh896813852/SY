package com.edusoho.yunketang.base;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.R;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.base.annotation.Translucent;
import com.edusoho.yunketang.bean.User;
import com.edusoho.yunketang.helper.ToastHelper;
import com.edusoho.yunketang.utils.LogUtil;
import com.edusoho.yunketang.utils.NotchUtil;
import com.edusoho.yunketang.utils.statusbar.StatusBarUtil;

import java.lang.reflect.Field;

/**
 * @param <DataBinding> 数据绑定者
 * @author huhao on 2018/7/4
 */
public class BaseActivity<DataBinding extends ViewDataBinding> extends AppCompatActivity implements Handler.Callback {
    private Handler handler;
    private DataBinding dataBinding;

    public TextView titleView;
    public TextView rightButtonTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Layout layout = getClass().getAnnotation(Layout.class);
        if (layout != null) {
            dataBinding = DataBindingUtil.setContentView(this, layout.value());
            titleView = findViewById(R.id.actionbarTitle);
            if (titleView != null) {
                titleView.setText(layout.title());
            }
            rightButtonTextView = findViewById(R.id.textButton);
            if (rightButtonTextView != null) {
                rightButtonTextView.setText(layout.rightButton());
                rightButtonTextView.setOnClickListener(v -> onRightButtonClick());
            }

            ImageView rightButtonImageView = findViewById(R.id.imageButton);
            if (rightButtonImageView != null) {
                if (layout.rightButtonRes() != -1) {
                    rightButtonImageView.setImageDrawable(ContextCompat.getDrawable(this, layout.rightButtonRes()));
                }
                rightButtonImageView.setOnClickListener(v -> onRightButtonClick());
            }

            FrameLayout titleLayout = findViewById(R.id.titleLayout);
            if (titleLayout != null) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) titleLayout.getLayoutParams();
                params.setMargins(0, NotchUtil.getNotchHeight(this), 0, 0);
                titleLayout.setLayoutParams(params);
            }
        } else {
            LogUtil.w("DataBinding警告->需要使用数据绑定功能必须使用@Layout注解注入布局");
        }

        // 绑定布局
        bindViewModelToUI();

        // 透明状态栏
        Translucent translucentAnnotation = getClass().getAnnotation(Translucent.class);
        if (translucentAnnotation != null) {
            if (translucentAnnotation.value()) {
                // 设置状态栏透明，沉浸式
                StatusBarUtil.setTranslucentStatus(this);
            }
        } else {
            // 设置状态栏背景颜色为灰色
            StatusBarUtil.setImmersiveStatusBar(this, true);
        }
    }

    /**
     * 返回控件被点击
     */
    public void onBackButtonClick(View view) {
        finish();
    }

    /**
     * 设置标题
     */
    public void setTitleView(String title) {
        if (titleView != null) {
            titleView.setText(title);
        }
    }

    /**
     * 设置右侧按钮文字
     */
    public void setRightButtonTextView(String content) {
        if (rightButtonTextView != null) {
            rightButtonTextView.setText(content);
        }
    }

    /**
     * 标题右侧文字或图片按钮点击事件
     */
    public void onRightButtonClick() {

    }

    /**
     * 使得XML文件中可以用viewModel
     */
    private void bindViewModelToUI() {
        if (dataBinding != null) {
            // 绑定两者
            Field[] declaredFields = dataBinding.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if (declaredField.getType().equals(getClass())) {
                    declaredField.setAccessible(true);
                    try {
                        declaredField.set(dataBinding, this);
                    } catch (IllegalAccessException e) {
                    }
                    declaredField.setAccessible(false);
                    break;
                }
            }
        }
    }

    /**
     * 获取Handler对象
     */
    public Handler getHandler() {
        if (handler == null) {
            handler = new Handler(this);
        }
        return handler;
    }

    /**
     * 处理 getHandler().setMessage()之后的回调处理
     */
    @Override
    public boolean handleMessage(Message message) {
        return false;
    }

    /**
     * 获取数据绑定对象
     */
    public DataBinding getDataBinding() {
        return dataBinding;
    }

    /**
     * 打开一个页面
     */
    public void startActivity(Class<? extends Activity> activityClass) {
        startActivity(activityClass, false);
    }

    /**
     * 打开一个页面
     * 并且设置成是否关闭当前页面
     */
    public void startActivity(Class<? extends Activity> activityClass, boolean finishSelf) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
        if (finishSelf) {
            finish();
        }
    }

    /**
     * 获取当前登录用户
     *
     * @return
     */
    public User getLoginUser() {
        return getMyApplication().getUser();
    }

    /**
     * 获取当前应用程序的全局变量
     *
     * @return
     */
    public SYApplication getMyApplication() {
        return (SYApplication) getApplication();
    }

    /**
     * 弹出一个提示信息框
     *
     * @param message
     */
    public void showToast(String message) {
        ToastHelper.showToast(this, message);
    }

    /**
     * 弹出一个提示信息框（去重）
     *
     * @param message
     */
    public void showSingleToast(String message) {
        ToastHelper.showSingleToast(this, message);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
