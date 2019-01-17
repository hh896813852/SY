package com.edusoho.yunketang.ui.common;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.internal.LinkedTreeMap;
import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.base.BaseActivity;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.edu.bean.DragCaptcha;
import com.edusoho.yunketang.edu.utils.GsonUtils;
import com.edusoho.yunketang.edu.utils.ImageUtils;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.utils.JsonUtil;
import com.edusoho.yunketang.utils.RequestCodeUtil;
import com.edusoho.yunketang.widget.ESContentLoadingLayout;
import com.edusoho.yunketang.widget.ValidateImageView;
import com.edusoho.yunketang.widget.slide.SlideView;

import org.json.JSONException;
import org.json.JSONObject;

@Layout(value = R.layout.activity_validate)
public class ValidateActivity extends BaseActivity {
    public static final int VALIDATE_CODE = RequestCodeUtil.next();
    public static final String COURSE_TYPE = "course_type";
    private int courseType; // 1、上元在线  2、上元会计

    private ValidateImageView vivCaptcha;
    private SlideView sbValidate;
    private ESContentLoadingLayout loadingView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseType = getIntent().getIntExtra(COURSE_TYPE, 0);
        initView();
        getValidatePicInfo();
    }

    private void initView() {
        sbValidate = findViewById(R.id.sv_validate);
        vivCaptcha = findViewById(R.id.viv_captcha);
        loadingView = findViewById(R.id.es_loading);
        sbValidate.setMax(10000);
        sbValidate.setSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                vivCaptcha.setMove(progress * 0.0001);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * 获取验证图片信息
     */
    private void getValidatePicInfo() {
        loadingView.showLoading();
        sbValidate.setEnabled(false);
        SYDataTransport.create(courseType == 1 ? SYConstants.ONLINE_GET_VALIDATE_PIC : SYConstants.ACCOUNTANT_GET_VALIDATE_PIC, false)
                .isJsonPost(false)
                .directReturn()
                .addHead("Accept", "application/vnd.edusoho.v2+json")
                .addParam("limitType", "mobile_register")
                .execute(new SYDataListener<String>() {

                    @Override
                    public void onSuccess(String data) {
                        loadingView.hideLoading();
                        DragCaptcha dragCaptcha = JsonUtil.fromJson(data, DragCaptcha.class);
                        onShowCaptcha(dragCaptcha);
                    }

                    @Override
                    public void onFail(int status, String failMessage) {
                        super.onFail(status, failMessage);
                        loadingView.hideLoading();
                    }
                });
    }

    public void onShowCaptcha(DragCaptcha dragCaptcha) {
        SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {

            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                try {
                    sbValidate.setEnabled(true);
                    initValidate();
                    String base64Text = dragCaptcha.getJigsaw();
                    final String pureBase64Encoded = base64Text.substring(base64Text.indexOf(",") + 1);
                    byte[] bytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                    final Bitmap verifiedBitmap = ImageUtils.bytes2Bitmap(bytes);
                    int width = getWindow().getDecorView().getWidth() - 2 * getResources().getDimensionPixelSize(R.dimen.captcha_margin);
                    vivCaptcha.setResource(resource, verifiedBitmap, width);
                    vivCaptcha.invalidate();
                    sbValidate.setSeekBarTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_UP:
                                    validateCaptcha(dragCaptcha.getToken(), (int) (resource.getWidth() * vivCaptcha.getBitmapDeltaX()) + "");
                                    break;
                            }
                            return false;
                        }
                    });
                } catch (Exception ex) {
                    Log.e("flag--", "onResourceReady: " + ex.getMessage());
                }
            }
        };
        if (!isDestroyed()) {
            Glide.with(this).load(dragCaptcha.getUrl()).asBitmap().into(target);
        }
    }

    /**
     * 验证
     */
    private void validateCaptcha(String token, String position) {
        LinkedTreeMap<String, String> tokenMap = new LinkedTreeMap<>();
        tokenMap.put("token", token);
        tokenMap.put("captcha", position);
        String json = GsonUtils.parseString(tokenMap).trim();
        byte[] bytes = json.getBytes();
        String base64Text = Base64.encodeToString(bytes, 0, bytes.length, Base64.NO_WRAP);
        String reverseBase64Text = new StringBuilder(base64Text).reverse().toString();
        SYDataTransport.create(String.format(courseType == 1 ? SYConstants.ONLINE_VALIDATE_CAPTCHA : SYConstants.ACCOUNTANT_VALIDATE_CAPTCHA, reverseBase64Text), false)
                .isGET()
                .execute(new SYDataListener<String>() {

                    @Override
                    public void onSuccess(String data) {
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            if (jsonObject.has("status") && jsonObject.getBoolean("status")) {
                                Intent intent = new Intent();
                                intent.putExtra("dragCaptchaToken", reverseBase64Text);
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            } else {
                                getValidatePicInfo();
                            }
                        } catch (JSONException e) {
                            getValidatePicInfo();
                        }
                    }
                });
    }

    private void initValidate() {
        vivCaptcha.init();
        sbValidate.setProgress(0);
    }

    /**
     * 返回
     */
    public void onFinishClick(View view) {
        finish();
    }
}
