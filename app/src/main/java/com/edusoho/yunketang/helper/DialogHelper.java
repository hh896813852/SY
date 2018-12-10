package com.edusoho.yunketang.helper;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.base.BaseDialog;
import com.edusoho.yunketang.utils.DialogUtil;

public class DialogHelper {

    /**
     * 显示账号不存在对话框
     */
    public static void showAccountNotExistDialog(Context context) {
        BaseDialog dialog = DialogUtil.showAnimationDialog(context, R.layout.dialog_login_no_account);
        dialog.show();
        dialog.findViewById(R.id.confirm).setOnClickListener(v -> {
            dialog.dismiss();
        });
    }

    /**
     * 显示账号已在其他平台存在的对话框
     */
    public static void showAccountExistDialog(Context context, int existPlatform) {
        SpannableString span1;
        SpannableString span2;
        RelativeSizeSpan sizeSpan1;
        RelativeSizeSpan sizeSpan2;
        StyleSpan styleSpan1;
        StyleSpan styleSpan2;
        BaseDialog dialog = DialogUtil.showAnimationDialog(context, R.layout.dialog_login_account_exist);
        TextView syjyView = dialog.findViewById(R.id.syjyView);
        TextView syzxView = dialog.findViewById(R.id.syzxView);
        TextView sykjView = dialog.findViewById(R.id.sykjView);
        TextView tipView = dialog.findViewById(R.id.tipView);
        dialog.findViewById(R.id.closeImage).setOnClickListener(v -> {
            dialog.dismiss();
        });
        switch (existPlatform) {
            case 0: // 在上元教育注册了
                // 改变TextView图片和颜色
                Drawable iconSYJY = ContextCompat.getDrawable(context, R.drawable.icon_syjy);
                iconSYJY.setBounds(0, 0, iconSYJY.getMinimumWidth(), iconSYJY.getMinimumHeight());
                syjyView.setCompoundDrawables(null, iconSYJY, null, null);
                syjyView.setTextColor(ContextCompat.getColor(context, R.color.text_black));
                // 改变TextView部分文字大小和样式
                span1 = new SpannableString("您在上元教育已有账号");
                span2 = new SpannableString("请选择上元教育登录");
                sizeSpan1 = new RelativeSizeSpan(1.2f); // 改变字体大小，放大1.2倍
                sizeSpan2 = new RelativeSizeSpan(1.2f); // 改变字体大小，放大1.2倍
                styleSpan1 = new StyleSpan(Typeface.BOLD); // 改变字体样式，加粗
                styleSpan2 = new StyleSpan(Typeface.BOLD); // 改变字体样式，加粗
                span1.setSpan(sizeSpan1, 2, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                span2.setSpan(sizeSpan2, 3, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                span1.setSpan(styleSpan1, 2, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                span2.setSpan(styleSpan2, 3, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tipView.setText(TextUtils.concat(span1, "，", span2));
                break;
            case 1: // 在上元在线注册了
                // 改变TextView图片和颜色
                Drawable iconSYZX = ContextCompat.getDrawable(context, R.drawable.icon_syzx);
                iconSYZX.setBounds(0, 0, iconSYZX.getMinimumWidth(), iconSYZX.getMinimumHeight());
                syzxView.setCompoundDrawables(null, iconSYZX, null, null);
                syzxView.setTextColor(ContextCompat.getColor(context, R.color.text_black));
                // 改变TextView部分文字大小和样式
                span1 = new SpannableString("您在上元在线已有账号");
                span2 = new SpannableString("请选择上元在线登录");
                sizeSpan1 = new RelativeSizeSpan(1.2f); // 改变字体大小
                sizeSpan2 = new RelativeSizeSpan(1.2f); // 改变字体大小
                styleSpan1 = new StyleSpan(Typeface.BOLD); // 改变字体样式
                styleSpan2 = new StyleSpan(Typeface.BOLD); // 改变字体样式
                span1.setSpan(sizeSpan1, 2, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                span2.setSpan(sizeSpan2, 3, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                span1.setSpan(styleSpan1, 2, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                span2.setSpan(styleSpan2, 3, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tipView.setText(TextUtils.concat(span1, "，", span2));
                break;
            case 2: // 在上元会计注册了
                // 改变TextView图片和颜色
                Drawable iconSYKJ = ContextCompat.getDrawable(context, R.drawable.icon_sykj);
                iconSYKJ.setBounds(0, 0, iconSYKJ.getMinimumWidth(), iconSYKJ.getMinimumHeight());
                sykjView.setCompoundDrawables(null, iconSYKJ, null, null);
                sykjView.setTextColor(ContextCompat.getColor(context, R.color.text_black));
                // 改变TextView部分文字大小和样式
                span1 = new SpannableString("您在上元会计已有账号");
                span2 = new SpannableString("请选择上元会计登录");
                sizeSpan1 = new RelativeSizeSpan(1.2f); // 改变字体大小
                sizeSpan2 = new RelativeSizeSpan(1.2f); // 改变字体大小
                styleSpan1 = new StyleSpan(Typeface.BOLD); // 改变字体样式
                styleSpan2 = new StyleSpan(Typeface.BOLD); // 改变字体样式
                span1.setSpan(sizeSpan1, 2, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                span2.setSpan(sizeSpan2, 3, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                span1.setSpan(styleSpan1, 2, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                span2.setSpan(styleSpan2, 3, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tipView.setText(TextUtils.concat(span1, "，", span2));
                break;
            case 3: // 在上元在线和上元会计注册了
                // 改变TextView图片和颜色
                Drawable iconSYZX2 = ContextCompat.getDrawable(context, R.drawable.icon_syzx);
                iconSYZX2.setBounds(0, 0, iconSYZX2.getMinimumWidth(), iconSYZX2.getMinimumHeight());
                syzxView.setCompoundDrawables(null, iconSYZX2, null, null);
                syzxView.setTextColor(ContextCompat.getColor(context, R.color.text_black));
                Drawable iconSYKJ2 = ContextCompat.getDrawable(context, R.drawable.icon_sykj);
                iconSYKJ2.setBounds(0, 0, iconSYKJ2.getMinimumWidth(), iconSYKJ2.getMinimumHeight());
                sykjView.setCompoundDrawables(null, iconSYKJ2, null, null);
                sykjView.setTextColor(ContextCompat.getColor(context, R.color.text_black));
                // 改变TextView部分文字大小和样式
                span1 = new SpannableString("您在上元在线和上元会计已有账号");
                span2 = new SpannableString("请选择上元在线或上元会计登录");
                sizeSpan1 = new RelativeSizeSpan(1.2f); // 改变字体大小
                sizeSpan2 = new RelativeSizeSpan(1.2f); // 改变字体大小
                styleSpan1 = new StyleSpan(Typeface.BOLD); // 改变字体样式
                styleSpan2 = new StyleSpan(Typeface.BOLD); // 改变字体样式
                span1.setSpan(sizeSpan1, 2, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                span2.setSpan(sizeSpan2, 3, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                span1.setSpan(styleSpan1, 2, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                span2.setSpan(styleSpan2, 3, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tipView.setText(TextUtils.concat(span1, "，", span2));
                break;
        }
    }

    private static TextView tipView;
    private static FrameLayout inputCodeLayout;
    private static EditText codeEdit;
    private static TextView codeView1;
    private static TextView codeView2;
    private static TextView codeView3;
    private static TextView codeView4;
    private static TextView codeView5;
    private static TextView codeView6;
    private static TextView cancelView;
    private static TextView sendCodeView;
    private static StringBuilder inputCode = new StringBuilder();

    /**
     * 去帮用户注册所需平台账号，获得其平台token值
     *
     * @param courseType 1、上元在线  2、上元会计
     */
    public static void showHelpRegisterDialog(Context context, int courseType, OnRegisterOtherPlatformListener onRegisterOtherPlatformListener) {
        BaseDialog dialog = DialogUtil.showAnimationDialog(context, R.layout.dialog_help_register, false);
        tipView = dialog.findViewById(R.id.tipView);
        inputCodeLayout = dialog.findViewById(R.id.inputCodeLayout);
        codeEdit = dialog.findViewById(R.id.codeEdit);
        codeView1 = dialog.findViewById(R.id.codeView1);
        codeView2 = dialog.findViewById(R.id.codeView2);
        codeView3 = dialog.findViewById(R.id.codeView3);
        codeView4 = dialog.findViewById(R.id.codeView4);
        codeView5 = dialog.findViewById(R.id.codeView5);
        codeView6 = dialog.findViewById(R.id.codeView6);
        cancelView = dialog.findViewById(R.id.cancelView);
        sendCodeView = dialog.findViewById(R.id.sendCodeView);
        tipView.setVisibility(View.VISIBLE);
        inputCodeLayout.setVisibility(View.GONE);
        cancelView.setOnClickListener(v -> dialog.dismiss());
        codeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changeEditStatus(context, dialog, courseType, onRegisterOtherPlatformListener);
            }
        });
        // 点击发送验证码
        sendCodeView.setOnClickListener(v -> {
            if (courseType == 1) {
                RegisterOtherPlatformHelper.SendSMSBySYZX(new RegisterOtherPlatformHelper.SendSMSListener() {
                    @Override
                    public void onSMSSendSuccess() {
                        tipView.setVisibility(View.GONE);
                        inputCodeLayout.setVisibility(View.VISIBLE);
                        // 开始倒计时重发
                        new MyCountDownTimer(context, "重新发送验证码", 60 * 1000, 1000).start();
                    }

                    @Override
                    public void onSMSSendFail() {
                        onRegisterOtherPlatformListener.onSMSError(sendCodeView);
                    }
                });
            }
            if (courseType == 2) {
                RegisterOtherPlatformHelper.SendSMSBySYKJ(new RegisterOtherPlatformHelper.SendSMSListener() {
                    @Override
                    public void onSMSSendSuccess() {
                        tipView.setVisibility(View.GONE);
                        inputCodeLayout.setVisibility(View.VISIBLE);
                        // 开始倒计时重发
                        new MyCountDownTimer(context, "重新发送验证码", 60 * 1000, 1000).start();
                    }

                    @Override
                    public void onSMSSendFail() {
                        onRegisterOtherPlatformListener.onSMSError(sendCodeView);
                    }
                });
            }

        });
    }

    /**
     * 改变输入框焦点和状态
     */
    private static void changeEditStatus(Context context, BaseDialog dialog, int courseType, OnRegisterOtherPlatformListener onRegisterOtherPlatformListener) {
        inputCode.delete(0, inputCode.length());
        inputCode = inputCode.append(codeEdit.getText());
        switch (inputCode.length()) {
            case 0:
                codeView1.setText("");
                codeView2.setText("");
                codeView3.setText("");
                codeView4.setText("");
                codeView5.setText("");
                codeView6.setText("");
                break;
            case 1:
                codeView1.setText(String.valueOf(inputCode.charAt(0)));
                codeView2.setText("");
                codeView3.setText("");
                codeView4.setText("");
                codeView5.setText("");
                codeView6.setText("");
                break;
            case 2:
                codeView1.setText(String.valueOf(inputCode.charAt(0)));
                codeView2.setText(String.valueOf(inputCode.charAt(1)));
                codeView3.setText("");
                codeView4.setText("");
                codeView5.setText("");
                codeView6.setText("");
                break;
            case 3:
                codeView1.setText(String.valueOf(inputCode.charAt(0)));
                codeView2.setText(String.valueOf(inputCode.charAt(1)));
                codeView3.setText(String.valueOf(inputCode.charAt(2)));
                codeView4.setText("");
                codeView5.setText("");
                codeView6.setText("");
                break;
            case 4:
                codeView1.setText(String.valueOf(inputCode.charAt(0)));
                codeView2.setText(String.valueOf(inputCode.charAt(1)));
                codeView3.setText(String.valueOf(inputCode.charAt(2)));
                codeView4.setText(String.valueOf(inputCode.charAt(3)));
                codeView5.setText("");
                codeView6.setText("");
                break;
            case 5:
                codeView1.setText(String.valueOf(inputCode.charAt(0)));
                codeView2.setText(String.valueOf(inputCode.charAt(1)));
                codeView3.setText(String.valueOf(inputCode.charAt(2)));
                codeView4.setText(String.valueOf(inputCode.charAt(3)));
                codeView5.setText(String.valueOf(inputCode.charAt(4)));
                codeView6.setText("");
                break;
            case 6:
                codeView1.setText(String.valueOf(inputCode.charAt(0)));
                codeView2.setText(String.valueOf(inputCode.charAt(1)));
                codeView3.setText(String.valueOf(inputCode.charAt(2)));
                codeView4.setText(String.valueOf(inputCode.charAt(3)));
                codeView5.setText(String.valueOf(inputCode.charAt(4)));
                codeView6.setText(String.valueOf(inputCode.charAt(5)));
                if (courseType == 1) { // 注册
                    RegisterOtherPlatformHelper.registerInSYZX(context, inputCode.toString(), new RegisterOtherPlatformHelper.RegisterListener() {
                        @Override
                        public void onRegisterSuccess() {
                            onRegisterOtherPlatformListener.registerSuccess();
                            dialog.dismiss();
                        }

                        @Override
                        public void onRegisterFail() {
                            ToastHelper.showSingleToast(context, "验证失败");
                        }
                    });
                }
                if (courseType == 2) {
                    RegisterOtherPlatformHelper.registerInSYKJ(context, inputCode.toString(), new RegisterOtherPlatformHelper.RegisterListener() {
                        @Override
                        public void onRegisterSuccess() {
                            onRegisterOtherPlatformListener.registerSuccess();
                            dialog.dismiss();
                        }

                        @Override
                        public void onRegisterFail() {
                            ToastHelper.showSingleToast(context, "验证失败");
                        }
                    });
                }
                break;
        }
    }

    /**
     * 验证码重新发送倒计时
     */
    static class MyCountDownTimer extends CountDownTimer {
        private String finishText;
        private Context context;

        public MyCountDownTimer(Context context, String finishText, long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            this.finishText = finishText;
            this.context = context;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            sendCodeView.setClickable(false);
            sendCodeView.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_dark_gray_bottom_right_corner_6));
            sendCodeView.setText((millisUntilFinished / 1000) + "s后重新发送验证码");
        }

        @Override
        public void onFinish() {
            sendCodeView.setClickable(true);
            sendCodeView.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_theme_color_bottom_right_corner_6));
            sendCodeView.setText(finishText);
        }
    }

    public interface OnRegisterOtherPlatformListener {
        void registerSuccess();

        void onSMSError(TextView sendCodeView);
    }
}
