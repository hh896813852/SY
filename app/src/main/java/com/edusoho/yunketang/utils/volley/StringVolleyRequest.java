package com.edusoho.yunketang.utils.volley;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.edusoho.yunketang.edu.bean.RequestUrl;

/**
 * Created by howzhi on 15/7/9.
 */
public class StringVolleyRequest extends BaseVolleyRequest<String> {

    public static final String TAG = "StringVolleyRequest";

    public StringVolleyRequest(int method, RequestUrl requestUrl, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, requestUrl, listener, errorListener);
    }

    @Override
    protected String getResponseData(NetworkResponse response) {
        String data = null;
        try {
            data = new String(response.data, "UTF-8");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return data;
    }
}
