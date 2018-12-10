package com.edusoho.yunketang.ui.login;

import android.app.Activity;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.base.BaseActivity;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.User;
import com.edusoho.yunketang.bean.base.Message;
import com.edusoho.yunketang.databinding.ActivityLoginBinding;
import com.edusoho.yunketang.helper.DialogHelper;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.utils.AppUtil;
import com.edusoho.yunketang.utils.JsonUtil;
import com.edusoho.yunketang.utils.RequestCodeUtil;
import com.edusoho.yunketang.utils.StringUtils;
import com.edusoho.yunketang.utils.encrypt.XXTEA;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zy on 2018/11/13 0013.
 */
@Layout(value = R.layout.activity_login)
public class LoginActivity extends BaseActivity<ActivityLoginBinding> {
    public static final int LOGIN_REQUEST_CODE = RequestCodeUtil.next();

    private String LOGIN_URL = SYConstants.USER_SYJY_LOGIN;

    public ObservableField<String> phoneNo = new ObservableField<>();
    public ObservableField<String> password = new ObservableField<>();
    public ObservableField<Boolean> passwordVisible = new ObservableField<>(false);
    public ObservableField<Boolean> canClick = new ObservableField<>(false);
    public ObservableField<Integer> loginType = new ObservableField<>(1);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        getDataBinding().phoneNoEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                canClick.set(!TextUtils.isEmpty(phoneNo.get()) && !TextUtils.isEmpty(password.get()));
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
                canClick.set(!TextUtils.isEmpty(phoneNo.get()) && !TextUtils.isEmpty(password.get()));
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
     * 登录
     */
    public void onLoginClick(View view) {
        if (!StringUtils.isMobilePhone(phoneNo.get())) {
            showSingleToast("请输入正确的手机号码！");
            return;
        }
        if (TextUtils.isEmpty(password.get())) {
            showSingleToast("请输入密码！");
            return;
        }
        SYDataTransport dataTransport = SYDataTransport.create(LOGIN_URL, false);
        dataTransport.addParam("mobile", phoneNo.get());
        if (loginType.get() == SYConstants.SYJY_LOGIN) {
            dataTransport.addParam("password", password.get());
        }
        if (loginType.get() == SYConstants.SYZX_LOGIN) {
            dataTransport.addParam("password", XXTEA.encryptToBase64String(password.get(), "www.233863.com"))
                    .addParam("enablePassword", password.get());
        }
        if (loginType.get() == SYConstants.SYKJ_LOGIN) {
            dataTransport.addParam("password", XXTEA.encryptToBase64String(password.get(), "www.sykjxy.com"))
                    .addParam("enablePassword", password.get());
        }
        dataTransport.addProgressing(this, "正在登录中...")
                .execute(new SYDataListener<User>() {

                    @Override
                    public void onMessage(Message<User> message) {
                        if (message.status == 1) {
                            super.onMessage(message);
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(String.valueOf(message.data));
                                int type = jsonObject.getInt("type");
                                if (type == -1) {
                                    onFail(message.status, message.msg);
                                } else {
                                    DialogHelper.showAccountExistDialog(LoginActivity.this, type);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onSuccess(User data) {
                        showSingleToast("登陆成功！");
                        // 关闭软键盘
                        AppUtil.closeSoftInputKeyBoard(LoginActivity.this);
                        data.mobile = phoneNo.get();
                        data.syzxUser = JsonUtil.fromJson(data.syzx_user,User.class);
                        SYApplication.getInstance().setUser(data);
                        setResult(Activity.RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFail(int status, String failMessage) {
                        if (failMessage.contains("手机号错误")) {
                            // 显示账号不存在对话框
                            DialogHelper.showAccountNotExistDialog(LoginActivity.this);
                        } else {
                            super.onFail(status, failMessage);
                        }
                    }
                }, User.class);
    }

    /**
     * 去注册
     */
    public void onToRegisterClick(View view) {
        startActivity(RegisterActivity.class);
    }

    /**
     * 登录方式
     */
    public void onLoginTypeClick(View view) {
        int type = Integer.valueOf(view.getTag().toString());
        loginType.set(type);
        switch (type) {
            case SYConstants.SYJY_LOGIN: // 上元教育
                LOGIN_URL = SYConstants.USER_SYJY_LOGIN;
                break;
            case SYConstants.SYZX_LOGIN: // 上元在线
                LOGIN_URL = SYConstants.USER_SYZX_LOGIN;
                break;
            case SYConstants.SYKJ_LOGIN: // 上元会计
                LOGIN_URL = SYConstants.USER_SYKJ_LOGIN;
                break;
        }
    }

    /**
     * 关闭
     */
    public void onCloseClick(View view) {
        finish();
    }
}
