package com.edusoho.yunketang.ui.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.base.BaseActivity;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.base.annotation.Translucent;
import com.edusoho.yunketang.databinding.ActivityShareBinding;
import com.edusoho.yunketang.wxapi.WXshare;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author huhao on 2017/6/9 0009.
 */
@Translucent
@Layout(value = R.layout.activity_share)
public class ShareActivity extends BaseActivity<ActivityShareBinding> {
    public static final String SHARE_URL = "share_url";
    public static final String SHARE_TITLE = "share_title";
    public static final String SHARE_CONTENT = "share_content";
    public static final String SHARE_THUMB_URL = "share_thumb_url";
    private String shareUrl;
    private String shareTitle;
    private String shareContent;
    private Bitmap shareThumb;
    private WXshare share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_out_from_bottom);
        getDataBinding().llView.startAnimation(animation);
        shareUrl = getIntent().getStringExtra(SHARE_URL);
        shareTitle = getIntent().getStringExtra(SHARE_TITLE);
        shareContent = getIntent().getStringExtra(SHARE_CONTENT);
        String shareThumbUrl = getIntent().getStringExtra(SHARE_THUMB_URL);
        if (TextUtils.isEmpty(shareUrl)) {
            showToast("分享链接有误！");
            finish();
            return;
        }
        share = new WXshare(this);
        share.register();
        // 加载课程图片并压缩
        loadBitMap(shareThumbUrl);
    }

    /**
     * 分享微信朋友圈
     */
    public void onWXCircleClick(View view) {
        share.shareUrl(WXshare.SHARE_TO_FRIEND_ZOOM, shareUrl, shareTitle, shareContent, shareThumb);
        finishActivity();
    }

    /**
     * 分享微信
     */
    public void onWXClick(View view) {
        share.shareUrl(WXshare.SHARE_TO_SESSION, shareUrl, shareTitle, shareContent, shareThumb);
        finishActivity();
    }

    /**
     * 取消
     */
    public void onCancelClicked(View view) {
        finishActivity();
    }

    /**
     * 底部退出动画
     */
    Animation animation = null;

    public void finishActivity() {
        if (null == animation) {
            animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_bottom);
            animation.setFillAfter(true);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    getDataBinding().layout.setBackgroundColor(Color.TRANSPARENT);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    finish();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            getDataBinding().llView.startAnimation(animation);
        }
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        share.unregister();
    }


    /**
     * 加载课程图片并压缩@param url
     */
    private void loadBitMap(String url) {
        new Thread(() -> {
            try {
                URL imageUrl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                shareThumb = BitmapFactory.decodeStream(is);
                // 尺寸压缩
                shareThumb = Bitmap.createScaledBitmap(shareThumb, 150, 150, true);
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
