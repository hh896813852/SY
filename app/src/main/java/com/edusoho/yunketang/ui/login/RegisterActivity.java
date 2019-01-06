package com.edusoho.yunketang.ui.login;

import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.base.BaseActivity;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.base.Message;
import com.edusoho.yunketang.databinding.ActivityRegisterBinding;
import com.edusoho.yunketang.helper.DialogHelper;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.utils.NotchUtil;
import com.edusoho.yunketang.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zy on 2018/11/13 0013.
 */
@Layout(value = R.layout.activity_register)
public class RegisterActivity extends BaseActivity<ActivityRegisterBinding> {

    public ObservableField<Integer> registerStep = new ObservableField<>(1);
    public ObservableField<String> phoneNo = new ObservableField<>();
    public ObservableField<String> password = new ObservableField<>();
    public ObservableField<String> verifyCodeText = new ObservableField<>("发送验证码");
    public ObservableField<String> verifyCode = new ObservableField<>();
    public ObservableField<Boolean> canSend = new ObservableField<>(true);
    public ObservableField<Boolean> passwordVisible = new ObservableField<>(false);
    public ObservableField<Boolean> canClick = new ObservableField<>(false);
    public ObservableField<String> registerText = new ObservableField<>("下一步");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        ScrollView.LayoutParams params = (ScrollView.LayoutParams) getDataBinding().contentLayout.getLayoutParams();
        params.setMargins(0, NotchUtil.getNotchHeight(this), 0, 0);
        getDataBinding().contentLayout.setLayoutParams(params);
        getDataBinding().phoneNoEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (registerStep.get() == 1) {
                    canClick.set(!TextUtils.isEmpty(phoneNo.get()));
                }
            }
        });
        getDataBinding().passwordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (registerStep.get() == 2) {
                    canClick.set(!TextUtils.isEmpty(verifyCode.get()) && !TextUtils.isEmpty(password.get()));
                }
            }
        });
        getDataBinding().verifyCodeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (registerStep.get() == 2) {
                    canClick.set(!TextUtils.isEmpty(verifyCode.get()) && !TextUtils.isEmpty(password.get()));
                }
            }
        });
    }

    /**
     * 密码是否可见
     */
    public void onPasswordVisibleClick(View view) {
        passwordVisible.set(!passwordVisible.get());
        getDataBinding().passwordEdit.setTransformationMethod(passwordVisible.get() ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
        getDataBinding().passwordEdit.setSelection(password.get().length());
    }

    /**
     * 下一步 / 注册
     */
    public void onRegisterClick(View view) {
        if (registerStep.get() == 1) {
            if (!StringUtils.isMobilePhone(phoneNo.get())) {
                showSingleToast("请输入正确的手机号码！");
                return;
            }
            // 验证手机号码是否正确
            validateMobile();
        } else {
            // 注册
            register();
        }
    }

    /**
     * 注册
     */
    private void register() {
        if (TextUtils.isEmpty(password.get())) {
            showSingleToast("密码不能为空");
            return;
        }
        if (!StringUtils.checkPasswordDigtAndLetter(password.get())) {
            showSingleToast("密码必须为6-20位字母数字组合");
            return;
        }
        if (TextUtils.isEmpty(verifyCode.get())) {
            showSingleToast("验证码不能为空");
            return;
        }
        SYDataTransport.create(SYConstants.USER_REGISTER)
                .addParam("mobile", phoneNo.get())
                .addParam("password", password.get())
                .addParam("code", verifyCode.get())
                .addProgressing(this, "正在注册...")
                .execute(new SYDataListener() {

                    @Override
                    public void onMessage(Message message) {
                        if (message.status == 1) {
                            showSingleToast("注册成功！");
                            finish();
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(String.valueOf(message.data));
                                int type = jsonObject.getInt("type");
                                if (type == -1) {
                                    super.onFail(message.status, message.msg);
                                } else {
                                    DialogHelper.showAccountExistDialog(RegisterActivity.this, type);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    /**
     * 返回登录
     */
    public void onBackToLoginClick(View view) {
        finish();
    }

    /**
     * 验证手机号码是否正确
     */
    public void validateMobile() {
        SYDataTransport.create(SYConstants.VALIDATE_MOBILE)
                .addParam("mobile", phoneNo.get())
                .addProgressing(this, "正在验证手机号码...")
                .execute(new SYDataListener() {

                    @Override
                    public void onMessage(Message message) {
                        if (message.status == 1) {
                            registerStep.set(2);
                            canClick.set(false);
                            registerText.set("完成");
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(String.valueOf(message.data));
                                int type = jsonObject.getInt("type");
                                if (type == -1) {
                                    super.onFail(message.status, message.msg);
                                } else {
                                    DialogHelper.showAccountExistDialog(RegisterActivity.this, type);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    /**
     * 发送验证
     */
    public void onSendVerifyCodeClick(View view) {
        SYDataTransport.create(String.format(SYConstants.SEND_SMS, phoneNo.get()))
                .isGET2()
                .addProgressing(this, "正在发送验证码...")
                .execute(new SYDataListener() {

                    @Override
                    public void onSuccess(Object data) {
                        // 开始倒计时重发
                        new MyCountDownTimer("重新发送", 60 * 1000, 1000).start();
                    }

                    @Override
                    public void onFail(int status, String failMessage) {
                        super.onFail(status, failMessage);
                    }
                });
    }

    /**
     * 验证码重新发送倒计时
     */
    class MyCountDownTimer extends CountDownTimer {
        private String finishText;

        public MyCountDownTimer(String finishText, long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            this.finishText = finishText;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            canSend.set(false);
            verifyCodeText.set((millisUntilFinished / 1000) + "s");
        }

        @Override
        public void onFinish() {
            canSend.set(true);
            verifyCodeText.set(finishText);
        }
    }
}
