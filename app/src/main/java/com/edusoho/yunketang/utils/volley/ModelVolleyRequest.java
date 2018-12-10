package com.edusoho.yunketang.utils.volley;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.edusoho.yunketang.edu.bean.RequestUrl;
import com.edusoho.yunketang.utils.RequestUtil;

/**
 * Created by suju on 16/10/28.
 */
public class ModelVolleyRequest<T> extends BaseVolleyRequest {

    private TypeToken<T> mTypeToken;

    public ModelVolleyRequest(int method,
                              RequestUrl requestUrl,
                              TypeToken<T> typeToken,
                              Response.Listener<T> listener,
                              Response.ErrorListener errorListener
    ) {
        super(method, requestUrl, listener, new InnerErrorListener(errorListener));
        this.mTypeToken = typeToken;
    }

    @Override
    protected T getResponseData(NetworkResponse response) {
        T t = null;
        try {
            String jsonStr = RequestUtil.handleRequestError(response.data);
            t = new Gson().fromJson(jsonStr, mTypeToken.getType());
        } catch (Exception e) {
            Log.e("getResponseData", e.getMessage());
        }
        return t;
    }

    public static class InnerErrorListener implements Response.ErrorListener {

        private Response.ErrorListener targetListener;

        public InnerErrorListener(Response.ErrorListener errorListener) {
            this.targetListener = errorListener;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            targetListener.onErrorResponse(error);
            if (error.networkResponse == null) {
                return;
            }
            try {
                if (TextUtils.isEmpty(RequestUtil.handleRequestError(error.networkResponse.data))) {
                    return;
                }
            } catch (RequestUtil.RequestErrorException re) {

            }
            if (targetListener != null) {
                targetListener.onErrorResponse(error);
            }
        }
    }

    ;
}
