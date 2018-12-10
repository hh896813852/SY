package com.edusoho.yunketang.edu.bean;

import android.os.Build;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by howzhi on 14-9-11.
 */
public class RequestUrl {

    public String url;
    public HashMap<String, String> heads;
    public Map<String, String> params;
    public HashMap<String, Object> muiltParams;

    public IdentityHashMap<String, String> muiltKeysMap;

    public RequestUrl() {
        heads = new HashMap<>();
        params = new LinkedHashMap<>();
        muiltParams = new HashMap<>();
        initHeads();
    }

    private void initHeads() {
        heads.put("User-Agent", String.format("%s%s%s", Build.MODEL, " Android-kuozhi ", Build.VERSION.SDK));
    }

    public RequestUrl(String url) {
        this();
        this.url = url;
    }

    public void setParams(String[] values) {
        if (values == null || values.length == 0) {
            return;
        }

        for (int i = 0; i < values.length; i = i + 2) {
            params.put(values[i], values[i + 1]);
        }
    }

    public void setGetParams(String[] values) {
        if (values == null || values.length == 0) {
            return;
        }
        StringBuffer sb = new StringBuffer(url);
        for (int i = 0; i < values.length; i += 2) {
            if (i == 0) {
                sb.append("?" + values[i] + "=" + values[i + 1]);
            } else {
                sb.append("&" + values[i] + "=" + values[i + 1]);
            }
        }
        url = sb.toString();
    }

    public void setMuiltParams(Object[] values) {
        if (values == null || values.length == 0) {
            return;
        }
        for (int i = 0; i < values.length; i = i + 2) {
            muiltParams.put(values[i].toString(), values[i + 1]);
        }
    }

    public void setParams(Map<String, String> p) {
        params = p;
    }

    public void setHeads(String[] values) {
        if (values == null || values.length == 0) {
            return;
        }
        for (int i = 0; i < values.length; i = i + 2) {
            heads.put(values[i], values[i + 1]);
        }
    }

    public void setHeads(HashMap<String, String> headers) {
        heads = headers;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public Map<String, Object> getAllParams() {
        muiltParams.putAll(params);
        return muiltParams;
    }

    public Map<String, String> getHeads() {
        return heads;
    }

    public IdentityHashMap<String, String> initKeysMap() {
        muiltKeysMap = new IdentityHashMap<>();
        return muiltKeysMap;
    }

    public IdentityHashMap<String, String> getMuiltKeyParams() {
        return muiltKeysMap;
    }

    public boolean isMuiltkeyParams() {
        return muiltKeysMap != null;
    }
}
