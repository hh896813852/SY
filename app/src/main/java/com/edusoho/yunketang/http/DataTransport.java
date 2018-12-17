package com.edusoho.yunketang.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;

import com.edusoho.yunketang.BuildConfig;
import com.edusoho.yunketang.bean.base.Message;
import com.edusoho.yunketang.utils.HttpUtil;
import com.edusoho.yunketang.utils.JsonUtil;
import com.edusoho.yunketang.utils.LogUtil;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 与后台进行数据传入的工具
 */
public class DataTransport {
    private static int requestId = 1;

    private ProgressDialog mProgressDialog;
    private Context context;
    private String progressContent;
    private boolean cancelable = true;
    private boolean directReturn = false;// 直接返回，不解析
    private boolean isGET = false;
    private boolean isGET2 = false;
    private boolean isJsonPost = true;
    private boolean needJsonParse = true; // 是否自动json解析为对象
    private String url;
    private Map<String, Object> params = new HashMap<>();
    private Map<String, String> heads = new HashMap<>();
    private Object dataType;

    DataTransport() {

    }

    /**
     * 直接返回，不解析
     */
    public DataTransport directReturn() {
        this.directReturn = true;
        return this;
    }

    /**
     * 是否是GET请求
     */
    public DataTransport GET() {
        this.isGET = true;
        return this;
    }

    /**
     * 是否是GET请求
     */
    public DataTransport isGET() {
        this.isGET = true;
        heads.put("Accept", "application/vnd.edusoho.v2+json");
        return this;
    }

    /**
     * 是否是GET请求
     */
    public DataTransport isGET2() {
        this.isGET2 = true;
        return this;
    }

    /**
     * 是否是json数据提交，默认是
     */
    public DataTransport isJsonPost(boolean isJsonPost) {
        this.isJsonPost = isJsonPost;
        return this;
    }

    public DataTransport addUrl(String url) {
        this.url = url;
        return this;
    }

    public DataTransport addParam(String key, Object value) {
        params.put(key, value);
        return this;
    }

    public DataTransport addParams(Map<String, Object> params) {
        for (String key : params.keySet()) {
            this.params.put(key, params.get(key));
        }
        return this;
    }

    public DataTransport addHead(String key, String value) {
        heads.put(key, value);
        return this;
    }

    public DataTransport addHeads(Map<String, String> heads) {
        for (String key : heads.keySet()) {
            this.heads.put(key, heads.get(key));
        }
        return this;
    }

    /**
     * 如果你需要一个默认的进度条
     *
     * @return
     */
    public DataTransport addDefaultProgressing(Context context) {
        this.context = context;
        this.progressContent = "正在加载,请稍等...";
        return this;
    }

    /**
     * 如果你需要一个自定义文字的进度条
     *
     * @return
     */
    public DataTransport addProgressing(Context context, String progressContent) {
        this.context = context;
        this.progressContent = progressContent;
        return this;
    }

    public void execute() {
        execute(null);
    }

    public void execute(DataListener dataListener) {
        needJsonParse = false;
        execute(dataListener, String.class);
    }

    public void execute(DataListener dataListener, Class dataClass) {
        this.dataType = dataClass;
        executeNow(dataListener);
    }

    public void execute(DataListener dataListener, TypeToken dataListToken) {
        this.dataType = dataListToken;
        executeNow(dataListener);
    }

    private void showProgress() {
        if (progressContent != null) {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(context);
                if (Build.VERSION.SDK_INT >= 21) {
                    mProgressDialog.setProgressStyle(android.R.style.Theme_Material_Light_Dialog);
                }
            } else {
                mProgressDialog.cancel();
            }
            mProgressDialog.setMessage(progressContent);
            mProgressDialog.setCancelable(cancelable);
            mProgressDialog.show();
        }
    }

    private void dismissProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    private void executeNow(DataListener dataListener) {
        showProgress();
        new AsyncTask<Void, Integer, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                String result;
                if (isGET) {
                    result = decode(HttpUtil.httpGet(url, heads));
                } else if (isGET2) {
                    result = decode(HttpUtil.httpGet2(url, heads));
                } else {
                    if (isJsonPost) {
                        result = decode(HttpUtil.jsonPost(url, params, heads));
                    } else {
                        result = decode(HttpUtil.httpPost(url, params, heads));
                    }
                }

                if (BuildConfig.DEBUG) {
                    LogUtil.i("request" + requestId + "/HTTP-URL", url);
                    LogUtil.i("request" + requestId + "/HTTP-HEADER", JsonUtil.toJson(heads));
                    LogUtil.i("request" + requestId + "/HTTP-PARAMS", JsonUtil.toJson(params));
                    if (result == null) {
                        LogUtil.i("request" + requestId + "/HTTP-RESULT", "null");
                    } else {
                        LogUtil.i("request" + requestId + "/HTTP-RESULT", result);
                    }
                    next();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                dismissProgress();
                Message message = new Message();
                if (TextUtils.isEmpty(result)) {
                    message.status = -1;
                    message.msg = "网络异常";
                } else {
                    try {
                        if (((isGET || isGET2) && !needJsonParse) || directReturn) {
                            message.status = 1;
                            message.data = result;
                        } else {
                            JSONObject jsonObject = new JSONObject(result);
                            message.msg = jsonObject.getString("msg");
                            message.retcode = jsonObject.getInt("code");
                            if (message.retcode == 200) {
                                String dataString = jsonObject.getString("data");
                                if (!TextUtils.isEmpty(dataString)) {
                                    JSONObject jsonObject2 = new JSONObject(dataString);
                                    message.status = jsonObject2.getInt("code");
                                    message.msg = jsonObject2.getString("msg");
                                    if (message.status == 1) {
                                        if (dataType.getClass().equals(Class.class)) { // 返回的是一个实体(非List)
                                            if (jsonObject2.has("data")) {
                                                if (dataType.equals(String.class)) {
                                                    message.data = jsonObject2.getString("data");
                                                } else {
                                                    Class typeClass = (Class) dataType;
                                                    message.data = JsonUtil.fromJson(jsonObject2.getString("data"), typeClass);
                                                }
                                            } else {
                                                if (dataType.equals(String.class)) {
                                                    message.data = dataString;
                                                } else {
                                                    Class typeClass = (Class) dataType;
                                                    message.data = JsonUtil.fromJson(dataString, typeClass);
                                                }
                                            }
                                        } else {                                       // 返回的是一个List
                                            TypeToken typeToken = ((TypeToken) dataType);
                                            message.data = JsonUtil.fromJson(jsonObject2.getString("data"), typeToken);
                                        }
                                    } else {
                                        message.data = dataString;
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        if (message.status != 1) {
                            message.status = -2;
                            message.data = "数据解析异常";
                        }
                    }
                }
                if (dataListener != null) {
                    dataListener.onMessage(message);
                }
            }
        }.execute();
    }

    private static final Pattern reUnicode = Pattern.compile("\\\\u([0-9a-zA-Z]{4})");

    public static String decode(String s) {
        if (s != null) {
            Matcher m = reUnicode.matcher(s);
            StringBuffer sb = new StringBuffer(s.length());
            while (m.find()) {
                m.appendReplacement(sb,
                        Character.toString((char) Integer.parseInt(m.group(1), 16)));
            }
            m.appendTail(sb);
            return sb.toString();
        }
        return null;
    }

    public synchronized static int next() {
        return requestId++;
    }
}
