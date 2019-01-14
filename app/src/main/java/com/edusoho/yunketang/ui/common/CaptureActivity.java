/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.edusoho.yunketang.ui.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.utils.DensityUtil;
import com.edusoho.yunketang.utils.GPSUtils;
import com.edusoho.yunketang.utils.NotchUtil;
import com.edusoho.yunketang.utils.RequestCodeUtil;
import com.google.gson.JsonObject;
import com.google.zxing.Result;
import com.edusoho.yunketang.R;
import com.edusoho.yunketang.base.BaseActivity;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.base.annotation.Translucent;
import com.edusoho.yunketang.utils.zxing.camera.CameraManager;
import com.edusoho.yunketang.utils.zxing.utils.BeepManager;
import com.edusoho.yunketang.utils.zxing.utils.CaptureActivityHandler;
import com.edusoho.yunketang.utils.zxing.utils.DecodeThread;
import com.edusoho.yunketang.utils.zxing.utils.InactivityTimer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 二维码扫描
 *
 * @author huangyin
 */
@Layout(value = R.layout.activity_capture)
@Translucent
public final class CaptureActivity extends BaseActivity implements SurfaceHolder.Callback {
    private static final String TAG = CaptureActivity.class.getSimpleName();
    public static int REQUEST_FROM_CHECK_IN = RequestCodeUtil.next();
    public static final String EXTRAS_STRING_CHECK_IN = "check_in"; // 考勤返回信息
    // Request Code & Extras Tag
    private static final String EXTRAS_STRING_FLAG_HANDLE_RESULT = "dealProxy"; // 是否处理结果Tag
    // private
    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;

    private SurfaceView scanPreview = null;
    private RelativeLayout scanContainer;
    private RelativeLayout scanCropView;
    private ImageView scanLine;
    private View statusView;
    private ImageView topShadowView;

    private Rect mCropRect = null;
    private boolean isHasSurface = false;
    private String handleResult = "1"; // 是否由CaptureActivity自己来处理扫描结果
    private String lat;
    private String lng;
    private String result; // 扫码结果

    /**
     * 二维码扫描到结果后的回调逻辑
     *
     * @param rawResult The contents of the barcode.
     * @param bundle    The extras
     */
    public void handleDecode(Result rawResult, Bundle bundle) {
        // 提示成功
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();
        // 处理结果
        result = rawResult.getText();
        // 考勤
        checkingIn();
//        if ("0".equals(handleResult)) { // 由打开二维码扫描控件的程序自行处理
//            showToast("0");
//        } else { // 提示用户扫出来的内容
//            Dialog dialog = DialogUtil.showDialog(CaptureActivity.this, "提示", result,
//                    "确定", (dialogInterface, i) -> finish(), "复制", (dialogInterface, i) -> {
//                        ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//                        ClipData myClip = ClipData.newPlainText("showText", result);
//                        myClipboard.setPrimaryClip(myClip);
//                        showToast("已复制");
//                        finish();
//                    }, true);
//            dialog.setOnDismissListener(dialogInterface -> finish());
//        }
    }

    /**
     * 考勤
     */
    private void checkingIn() {
        if (SYApplication.getInstance().getUser() == null || SYApplication.getInstance().getUser().syjyUser == null) {
            showSingleToast("用户不存在！");
            finish();
            return;
        }
        if (TextUtils.isEmpty(result)) {
            showSingleToast("未扫到任何结果！");
            finish();
            return;
        }
        if (TextUtils.isEmpty(lng) || TextUtils.isEmpty(lat)) {
            showSingleToast("未能获取您的位置！");
            finish();
            return;
        }
        SYDataTransport.create(String.format(SYConstants.CHECKING_IN, result, SYApplication.getInstance().getUser().syjyUser.id, lng, lat))
                .isGET()
                .directReturn()
                .execute(new SYDataListener<String>() {

                    @Override
                    public void onSuccess(String data) {
                        // {"error":-3,"errorInfo":"正在上课中！"}
                        // {"error":0,"errorInfo":"当前时间内不能考勤！"}
                        // {"error":1,"errorInfo":"考勤成功！"}
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            if (jsonObject.has("error")) {
                                int errorId = jsonObject.getInt("error");
                                String checkInfo = jsonObject.getString("errorInfo");
//                                if (errorId == 0) {
//                                    showSingleToast(jsonObject.getString("errorInfo"));
//                                } else if (errorId == 1) {
//                                    showSingleToast(jsonObject.getString("errorInfo"));
//                                }
                                Intent intent = new Intent();
                                intent.putExtra(CaptureActivity.EXTRAS_STRING_CHECK_IN, checkInfo);
                                setResult(Activity.RESULT_OK, intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        finish();
                    }
                }, String.class);
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        handleResult = getIntent().getStringExtra(EXTRAS_STRING_FLAG_HANDLE_RESULT);

        scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
        scanContainer = (RelativeLayout) findViewById(R.id.capture_container);
        scanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
        scanLine = (ImageView) findViewById(R.id.capture_scan_line);
        statusView = findViewById(R.id.statusView);
        topShadowView = findViewById(R.id.topShadowView);

        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);

        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation
                .RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                0.9f);
        animation.setDuration(4500);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        scanLine.startAnimation(animation);

        // GPS定位
        getLocation();

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) statusView.getLayoutParams();
        params.height = NotchUtil.getNotchHeight(this);
        statusView.setLayoutParams(params);

        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) topShadowView.getLayoutParams();
        params2.setMargins(0, NotchUtil.getNotchHeight(this) + DensityUtil.dip2px(this, 60), 0, 0);
        topShadowView.setLayoutParams(params2);
    }

    /**
     * 定位
     */
    private void getLocation() {
        GPSUtils.getInstance(this).getLngAndLat(new GPSUtils.OnLocationResultListener() {
            @Override
            public void onLocationResult(Location location) {
                lng = String.valueOf(location.getLongitude());
                lat = String.valueOf(location.getLatitude());
            }

            @Override
            public void OnLocationChange(Location location) {
                lng = String.valueOf(location.getLongitude());
                lat = String.valueOf(location.getLatitude());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // CameraManager must be initialized here, not in onCreate(). This is
        // necessary because we don't
        // want to open the camera driver and measure the screen size if we're
        // going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the
        // wrong size and partially
        // off screen.
        cameraManager = new CameraManager(getApplication());

        handler = null;

        if (isHasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(scanPreview.getHolder());
        } else {
            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            scanPreview.getHolder().addCallback(this);
        }

        inactivityTimer.onResume();
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        if (!isHasSurface) {
            scanPreview.getHolder().removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
        GPSUtils.getInstance(this).removeListener();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!isHasSurface) {
            isHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isHasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, cameraManager, DecodeThread.ALL_MODE);
            }

            initCrop();
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        // camera error
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage("打开摄像头失败");
        builder.setPositiveButton("确定", (dialog, which) -> finish());
        builder.setOnCancelListener(dialog -> finish());
        builder.show();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
    }

    public Rect getCropRect() {
        return mCropRect;
    }

    /**
     * 初始化截取的矩形区域
     */
    private void initCrop() {
        int cameraWidth = cameraManager.getCameraResolution().y;
        int cameraHeight = cameraManager.getCameraResolution().x;

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    @Override
    public Handler getHandler() {
        return handler;
    }

    private boolean isFlashOn = false;

    /**
     * 打开闪光灯
     */
    @Override
    public void onRightButtonClick() {
        try {
            boolean isSuccess = cameraManager.setFlashLight(!isFlashOn);
            if (!isSuccess) {
                showToast("暂时无法开启闪光灯");
                return;
            }
            if (isFlashOn) {
                // 关闭闪光灯
                setRightButtonTextView("开启闪光灯");
                isFlashOn = false;
            } else {
                // 开启闪光灯
                setRightButtonTextView("关闭闪光灯");
                isFlashOn = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}