package com.edusoho.yunketang.helper;

import android.content.Context;
import android.text.TextUtils;

import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.bean.User;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.utils.JsonUtil;
import com.edusoho.yunketang.utils.encrypt.XXTEA;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterOtherPlatformHelper {
    private static String smsToken;
    private static String dragCaptchaToken = "";

    public static void setToken(String token) {
        dragCaptchaToken = token;
    }

    /**
     * 发送短信验证码（注册上元在线）
     */
    public static void SendSMSBySYZX(SendSMSListener sendSMSListener) {
        SYDataTransport.create(SYConstants.SEND_ONLINE_SMS, false)
                .isJsonPost(false)
                .directReturn()
                .addHead("Accept", "application/vnd.edusoho.v2+json")
                .addParam("type", "register")
                .addParam("mobile", SYApplication.getInstance().getUser().mobile)
                .addParam("dragCaptchaToken", dragCaptchaToken)
                .execute(new SYDataListener<String>() {
                    @Override
                    public void onSuccess(String data) {
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            smsToken = jsonObject.getString("smsToken");
                            sendSMSListener.onSMSSendSuccess();
                        } catch (JSONException e1) {
                            try {
                                JSONObject jsonObject = new JSONObject(data);
                                int code = jsonObject.getInt("code");
                                if (code == 403) {
                                    sendSMSListener.onSMSSendFail();
                                }
                            } catch (JSONException e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                });
    }

    /**
     * 发送短信验证码（注册上元会计）
     */
    public static void SendSMSBySYKJ(SendSMSListener sendSMSListener) {
        SYDataTransport.create(SYConstants.SEND_ACCOUNTANT_SMS, false)
                .isJsonPost(false)
                .directReturn()
                .addHead("Accept", "application/vnd.edusoho.v2+json")
                .addParam("type", "register")
                .addParam("mobile", SYApplication.getInstance().getUser().mobile)
                .addParam("dragCaptchaToken", dragCaptchaToken)
                .execute(new SYDataListener<String>() {
                    @Override
                    public void onSuccess(String data) {
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            smsToken = jsonObject.getString("smsToken");
                            sendSMSListener.onSMSSendSuccess();
                        } catch (JSONException e) {
                            try {
                                JSONObject jsonObject = new JSONObject(data);
                                int code = jsonObject.getInt("code");
                                if (code == 403) {
                                    sendSMSListener.onSMSSendFail();
                                }
                            } catch (JSONException e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                });
    }

    /**
     * 注册（上元在线）
     */
    public static void registerInSYZX(Context context, String smsCode, RegisterListener registerListener) {
        if (TextUtils.isEmpty(smsToken)) {
            return;
        }
        SYDataTransport.create(SYConstants.USER_ONLINE_REGISTER, false)
                .isJsonPost(false)
                .directReturn()
                .addHead("Accept", "application/vnd.edusoho.v2+json")
                .addParam("smsToken", smsToken)
                .addParam("mobile", SYApplication.getInstance().getUser().mobile)
                .addParam("smsCode", smsCode)
                .addParam("encrypt_password", XXTEA.encryptToBase64String("111111", "www.233863.com"))
                .addProgressing(context, "正在验证身份...")
                .execute(new SYDataListener<String>() {
                    @Override
                    public void onSuccess(String data) {
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            if (jsonObject.has("code") && jsonObject.getInt("code") == 403) {
                                registerListener.onRegisterFail();
                            } else {
                                User user = JsonUtil.fromJson(data, User.class);
                                User currentUser = SYApplication.getInstance().getUser();
                                currentUser.syzxToken = user.token;
                                SYApplication.getInstance().reSaveUser();
                                registerListener.onRegisterSuccess();
                            }
                        } catch (JSONException e) {
                            registerListener.onRegisterFail();
                        }
                    }

                    @Override
                    public void onFail(int status, String failMessage) {
                        registerListener.onRegisterFail();
                    }
                });
    }

    /**
     * 注册（上元会计）
     */
    public static void registerInSYKJ(Context context, String smsCode, RegisterListener registerListener) {
        if (TextUtils.isEmpty(smsToken)) {
            return;
        }
        SYDataTransport.create(SYConstants.USER_ACCOUNTANT_REGISTER, false)
                .isJsonPost(false)
                .directReturn()
                .addHead("Accept", "application/vnd.edusoho.v2+json")
                .addParam("smsToken", smsToken)
                .addParam("mobile", SYApplication.getInstance().getUser().mobile)
                .addParam("smsCode", smsCode)
                .addParam("encrypt_password", XXTEA.encryptToBase64String("111111", "www.sykjxy.com"))
                .addProgressing(context, "正在验证身份...")
                .execute(new SYDataListener<String>() {
                    @Override
                    public void onSuccess(String data) {
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            if (jsonObject.has("code") && jsonObject.getInt("code") == 403) {
                                registerListener.onRegisterFail();
                            } else {
                                User user = JsonUtil.fromJson(data, User.class);
                                User currentUser = SYApplication.getInstance().getUser();
                                currentUser.sykjToken = user.token;
                                SYApplication.getInstance().reSaveUser();
                                registerListener.onRegisterSuccess();
                            }
                        } catch (JSONException e) {
                            registerListener.onRegisterFail();
                        }
                    }

                    @Override
                    public void onFail(int status, String failMessage) {
                        registerListener.onRegisterFail();
                    }
                }, User.class);
    }

    public interface SendSMSListener {
        void onSMSSendSuccess();

        void onSMSSendFail();
    }

    public interface RegisterListener {
        void onRegisterSuccess();

        void onRegisterFail();
    }
}
