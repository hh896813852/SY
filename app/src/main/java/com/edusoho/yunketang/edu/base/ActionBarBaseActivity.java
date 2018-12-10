package com.edusoho.yunketang.edu.base;


import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.edusoho.yunketang.edu.BaseActivity2;
import com.edusoho.yunketang.R;
import com.edusoho.yunketang.edu.bean.MessageType;
import com.edusoho.yunketang.edu.bean.WidgetMessage;
import com.edusoho.yunketang.edu.core.MessageEngine;
import com.edusoho.yunketang.edu.utils.ActivityUtil;
import com.edusoho.yunketang.edu.utils.Const;
import com.edusoho.yunketang.edu.widget.EduSohoCompoundButton;
import com.edusoho.yunketang.edu.widget.PopupDialog;
import com.edusoho.yunketang.widget.CircleImageView;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by JesseHuang on 15/4/23.
 * 用于包含ActionBar的theme
 */
public class ActionBarBaseActivity extends BaseActivity2 implements MessageEngine.MessageCallback {

    public static final String TAG  = "ActionBarBaseActivity";
    public static final String BACK = "返回";
    public ActionBar mActionBar;
    protected TextView              mTitleTextView;
    private   View                  titleLayoutView;
    private   View                  mTitleLoading;
    private EduSohoCompoundButton switchButton;
    private   RadioButton           rbStudyRadioButton;
    private   RadioButton           rbDiscussRadioButton;
    private CircleImageView civBadgeView;
    private Queue<WidgetMessage> mUIMessageQueue;

    protected int         mRunStatus;
    protected PopupDialog mNoticeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBar = getSupportActionBar();
//        registMessageSource(this);
        mUIMessageQueue = new ArrayDeque<>();
        if (mActionBar != null) {
            mActionBar.setWindowTitle("title");
        }
        ActivityUtil.setStatusBarTranslucent(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ActivityUtil.setRootViewFitsWindow(this, getStatusBarColor());
    }

    protected int getStatusBarColor() {
        return getResources().getColor(R.color.primary);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ActivityUtil.setRootViewFitsWindow(this, getResources().getColor(R.color.primary));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRunStatus = MSG_RESUME;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRunStatus = MSG_PAUSE;
    }

    @Override
    public void setSupportActionBar(Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowTitleEnabled(false);
    }

    @Override
    public void setTitle(CharSequence title) {
        setBackMode(BACK, title.toString());
    }

    public void setBackMode(String backTitle, String title) {
        titleLayoutView = getLayoutInflater().inflate(R.layout.actionbar_custom_title, null);
        mTitleTextView = (TextView) titleLayoutView.findViewById(R.id.tv_action_bar_title);
        mTitleTextView.setText(title);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        mActionBar.setCustomView(titleLayoutView, layoutParams);

        if (backTitle != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        app.unRegistMsgSource(this);
        mUIMessageQueue.clear();
        if (mNoticeDialog != null) {
            mNoticeDialog.dismiss();
            mNoticeDialog = null;
        }
    }

    protected void invokeUIMessage() {
        WidgetMessage message;
        while ((message = mUIMessageQueue.poll()) != null) {
            invoke(message);
        }
    }

    protected void saveMessage(WidgetMessage message) {
        mUIMessageQueue.add(message);
    }

    @Override
    public void invoke(WidgetMessage message) {
    }

    protected int getRunStatus() {
        return mRunStatus;
    }

    protected void processMessage(WidgetMessage message) {
        MessageType messageType = message.type;
        if (Const.TOKEN_LOSE.equals(messageType.type)) {
            if (mNoticeDialog != null) {
                mNoticeDialog.dismiss();
            }
            mNoticeDialog = PopupDialog.createNormal(mActivity, "提示", getString(R.string.token_lose_notice));
            mNoticeDialog.setOkListener(new PopupDialog.PopupClickListener() {
                @Override
                public void onClick(int button) {
                    handleTokenLostMsg();
                    finish();
                }
            });

            mNoticeDialog.show();
        }
    }

    protected void handleTokenLostMsg() {
//        Bundle bundle = new Bundle();
//        bundle.putString(Const.BIND_USER_ID, "");
//
//        getAppSettingProvider().setUser(null);
//        new IMServiceProvider(getBaseContext()).unBindServer();
//        app.removeToken();
//        MessageEngine.getInstance().sendMsg(Const.LOGOUT_SUCCESS, null);
//        MessageEngine.getInstance().sendMsgToTaget(Const.SWITCH_TAB, null, DefaultPageActivity.class);
    }

    @Override
    public MessageType[] getMsgTypes() {
        return new MessageType[0];
    }

    @Override
    public int getMode() {
        return REGIST_CLASS;
    }
}
