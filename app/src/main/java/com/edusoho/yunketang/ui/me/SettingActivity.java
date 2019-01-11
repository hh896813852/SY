package com.edusoho.yunketang.ui.me;

import android.content.Intent;
import android.databinding.ObservableField;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.base.BaseActivity;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.Setting;
import com.edusoho.yunketang.databinding.ActivitySettingBinding;
import com.edusoho.yunketang.helper.AppPreferences;
import com.edusoho.yunketang.ui.login.LoginActivity;
import com.edusoho.yunketang.utils.AppUtil;
import com.edusoho.yunketang.utils.BitmapUtil;
import com.edusoho.yunketang.utils.html.MyHtmlTagHandler;

@Layout(value = R.layout.activity_setting, title = "设置")
public class SettingActivity extends BaseActivity<ActivitySettingBinding> {

    public ObservableField<String> version = new ObservableField<>(); // 版本号
    public ObservableField<Integer> pageStatus = new ObservableField<>(1);           // 1、主界面 2、接受新消息设置界面 3、关于上元教育界面
    public ObservableField<Boolean> isAllow4GPlay = new ObservableField<>();    // 是否允许4G播放
    public ObservableField<String> newMsgStatus = new ObservableField<>("已打开");   // 是否允许声音提示
    public ObservableField<Boolean> isAllowSound = new ObservableField<>();      // 是否允许声音提示
    public ObservableField<Boolean> isAllowVibration = new ObservableField<>();  // 是否震动
    public ObservableField<Boolean> isLogin = new ObservableField<>(false);  // 是否登录
    private Setting setting; // 设置配置

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void showHtml(TextView textView, String content) {
        CharSequence html = Html.fromHtml(content, source -> {
            Drawable drawable = new BitmapDrawable(getResources(), BitmapUtil.base64ToBitmap(source));
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            return drawable;
        }, new MyHtmlTagHandler("syfont"));
        SpannableStringBuilder spanBuilder = new SpannableStringBuilder(html);
        textView.setText(spanBuilder);
        //设置可以点击超连接
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void initView() {
        isLogin.set(SYApplication.getInstance().isLogin());
        // 如果不在HTML标签最前面加入其他HTML元素，此函数可能不会生效，原因未知。
        String htmlStr = "<br/><font size=\"15px\" color=\"red\">This is some text!</font><br/><font size=\"18px\" color=\"blue\">This is some text!</font>";
        htmlStr = htmlStr.replace("font","syfont");
        showHtml(getDataBinding().aboutUsView, htmlStr);

        version.set("版本" + AppUtil.getAppVersionName(this));
        setting = AppPreferences.getSettings();
        isAllow4GPlay.set(setting.isAllow4GPlay == 1);
        isAllowSound.set(setting.soundSwitchState == 1);
        isAllowVibration.set(setting.vibrationSwitchState == 1);
        // 设置消息通知状态
        setNewMsgStatus();

        getDataBinding().playSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isAllow4GPlay.set(isChecked);
            setting.isAllow4GPlay = isChecked ? 1 : 0;
            AppPreferences.saveSettings(setting);
        });
        getDataBinding().soundSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isAllowSound.set(isChecked);
            setting.soundSwitchState = isChecked ? 1 : 0;
            AppPreferences.saveSettings(setting);
            // 设置消息通知状态
            setNewMsgStatus();
        });
        getDataBinding().vibrationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isAllowVibration.set(isChecked);
            setting.vibrationSwitchState = isChecked ? 1 : 0;
            AppPreferences.saveSettings(setting);
            // 设置消息通知状态
            setNewMsgStatus();
        });
    }

    /**
     * 设置消息通知状态
     */
    private void setNewMsgStatus() {
        if (isAllowSound.get() || isAllowVibration.get()) {
            newMsgStatus.set("已打开");
        } else {
            newMsgStatus.set("已关闭");
        }
    }

    /**
     * 新消息通知设置
     */
    public void onNewMsgSettingClick(View view) {
        pageStatus.set(2);
    }

    /**
     * 关于上元教育
     */
    public void onAboutSYClick(View view) {
        pageStatus.set(3);
        setTitleView("关于上元教育");
    }

    /**
     * 退出登录
     */
    public void onLogoutClick(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        SYApplication.getInstance().setUser(null);
        finish();
    }

    @Override
    public void onBackButtonClick(View view) {
        if (pageStatus.get() > 1) {
            pageStatus.set(1);
            setTitleView("设置");
        } else {
            super.onBackButtonClick(view);
        }
    }

    @Override
    public void onBackPressed() {
        onBackButtonClick(null);
    }
}
