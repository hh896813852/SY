package com.edusoho.yunketang.edu.request.model;

import android.util.Log;

import java.net.URL;
import java.util.HashMap;

/**
 * Created by howzhi on 15/4/28.
 */
public class Request {

    public HashMap<String, Object> params;
    public HashMap<String, Object> heads;
    public String url;
    private String mPath;
    private String mHost;

    private URL mRequestURL;

    public Request(String url)
    {
        this.url = url;
        initRequest();
    }

    public String getPath()
    {
        if (mPath == null) {
            mPath = mRequestURL == null ? "" : mRequestURL.getPath();
        }
        return mPath;
    }

    public String getName()
    {
        String path = getPath();
        int lastDirPoint = path.lastIndexOf('/');
        if (lastDirPoint != -1) {
            return path.substring(lastDirPoint + 1);
        }

        return null;
    }

    /**
     * 获取parentPath下path
     * @param parentPath
     * @return
     */
    public String getPath(String parentPath)
    {
        if (parentPath == null) {
            return "";
        }

        String path = getPath();
        int lastDirPoint = path.lastIndexOf(parentPath);
        if (lastDirPoint != -1) {
            return path.substring(lastDirPoint + parentPath.length() + 1);
        }

        return path;
    }

    public String getDir()
    {
        String path = getPath();
        int lastDirPoint = path.lastIndexOf('/');
        int firstDirPoint = path.indexOf('/');
        if (firstDirPoint == lastDirPoint) {
            return null;
        }
        return path.substring(0, lastDirPoint);
    }

    public String getHost()
    {
        if (mHost == null) {
            mHost = mRequestURL == null ? "" : mRequestURL.getHost();
        }
        return mHost;
    }

    private URL getRequestURL()
    {
        URL requestUrl = null;
        try {
            requestUrl = new URL(url);
        } catch (Exception e) {
            Log.e(null, e.toString());
        }

        return requestUrl;
    }

    public void destory()
    {
        this.params.clear();
        this.heads.clear();
    }

    protected void initRequest()
    {
        this.params = new HashMap<>();
        this.heads = new HashMap<>();
        mRequestURL = getRequestURL();
        mPath = getPath();
        mHost = getHost();
    }
}
