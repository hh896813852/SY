package com.edusoho.yunketang.edu;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.edu.base.ActionBarBaseActivity;
import com.edusoho.yunketang.edu.bean.MessageType;
import com.edusoho.yunketang.edu.bean.WidgetMessage;
import com.edusoho.yunketang.edu.core.MessageEngine;
import com.edusoho.yunketang.edu.utils.CommonUtil;
import com.edusoho.yunketang.edu.utils.Const;
import com.edusoho.yunketang.edu.webview.ESWebView;
import com.edusoho.yunketang.edu.webview.bridgeadapter.bridge.BridgePluginContext;

/**
 * Created by JesseHuang on 15/6/17.
 */
public class WebViewActivity extends ActionBarBaseActivity {

    private final static String TAG        = "WebViewActivity";
    public final static  int    CLOSE      = 0x01;
    public final static  String SEND_EVENT = "send_event";
    public static final  int    BACK       = 0x02;

    private String url = "";
    private ESWebView mWebView;
    private Toolbar mToolbar;
    private TextView  mTitleView;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);
        mToolbar = (Toolbar) findViewById(R.id.tb_webview_toolbar);
        mTitleView = (TextView) findViewById(R.id.tv_toolbar_title);
        initCordovaWebView();
    }

    public void initCordovaWebView() {
        Intent intent = getIntent();
        if (intent != null) {
            url = intent.getStringExtra(Const.WEB_URL);
        }

        if (TextUtils.isEmpty(url)) {
            CommonUtil.longToast(mActivity, "访问的地址不存在");
            return;
        }

        if (!url.startsWith(app.host) || url.contains(Const.HTML5_POINT_INFO)) {
            setSupportActionBar(mToolbar);
            mToolbar.setVisibility(View.VISIBLE);
        }
        mWebView = (ESWebView) findViewById(R.id.webView);
        mWebView.initPlugin(mActivity);
        mWebView.loadUrl(url);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitleView.setText(title);
    }

    @Override
    public void invoke(WidgetMessage message) {
        processMessage(message);
        MessageType messageType = message.type;

        if (SEND_EVENT.equals(messageType.type)) {
            Bundle bundle = message.data;
            String eventName = bundle.getString("event");
            mWebView.getWebView().execJsScript(String.format("jsBridgeAdapter.sendEvent('%s')", eventName));
        }
        if (ESWebView.MAIN_UPDATE.equals(messageType.type)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.reload();
                }
            });
            return;
        }
        if (Const.THIRD_PARTY_LOGIN_SUCCESS.equals(messageType.type) || Const.LOGIN_SUCCESS.equals(messageType.type)) {
            if (getRunStatus() == MSG_PAUSE) {
                saveMessage(message);
                return;
            }

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mWebView.reload();
                }
            });
            return;
        }
        if (messageType.code == BACK) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                        return;
                    }
                    finish();
                }
            });
        } else if (messageType.code == CLOSE) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        invokeUIMessage();
    }

    @Override
    public int getMode() {
        return REGIST_OBJECT;
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        MessageEngine.getInstance().sendMsg(Const.WEB_BACK_REFRESH, null);
        mWebView = null;
    }

    @Override
    public MessageType[] getMsgTypes() {
        String source = this.getClass().getSimpleName();
        MessageType[] messageTypes = new MessageType[]{
                new MessageType(CLOSE, source),
                new MessageType(Const.TOKEN_LOSE),
                new MessageType(Const.LOGIN_SUCCESS),
                new MessageType(Const.THIRD_PARTY_LOGIN_SUCCESS),
                new MessageType(SEND_EVENT),
                new MessageType(ESWebView.MAIN_UPDATE)
        };
        return messageTypes;
    }

    @Override
    public void finish() {
        Log.d(TAG, "finish");
        super.finish();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                destoryVideoResource();
                destoryWebView();
            }
        });
    }

    private void destoryWebView() {
        if (mWebView != null) {
            mWebView.destroy();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        BridgePluginContext pluginContext = mWebView.getWebView().getBridgePluginContext();
        pluginContext.onActivityResult(requestCode, resultCode, data);
    }

    private void destoryVideoResource() {
        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        audioManager.requestAudioFocus(
                new AudioManager.OnAudioFocusChangeListener() {
                    @Override
                    public void onAudioFocusChange(int i) {
                        //nothing
                    }
                },
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
        );

        Log.i(null, "WebVideoActivity webview stop");
        try {
            mWebView.getWebView().onPause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
