package com.edusoho.yunketang.edu.request.model;

import java.io.InputStream;
import java.util.Map;

/**
 * Created by howzhi on 15/4/28.
 */
public class Response<T> {

    protected static final String TAG = "Response";
    protected String mMimeType;
    protected String mEncoding;
    protected int mStatusCode;
    protected Map<String, String> mResponseHeaders;
    protected InputStream mInputStream;
    protected T mData;

    public Response() {
    }

    public void setResponse(Response response) {
        setMimeType(response.getMimeType());
        setContent(response.getContent());
        setEncoding(response.getEncoding());
    }

    public Response(T data) {
        this.mData = data;
    }

    public String getMimeType() {
        return mMimeType;
    }

    public void setMimeType(String mMimeType) {
        this.mMimeType = mMimeType;
    }

    public String getEncoding() {
        return mEncoding;
    }

    public void setEncoding(String mEncoding) {
        this.mEncoding = mEncoding;
    }

    public int getStatusCode() {
        return mStatusCode;
    }

    public void setStatusCode(int mStatusCode) {
        this.mStatusCode = mStatusCode;
    }

    public Map<String, String> getResponseHeaders() {
        return mResponseHeaders;
    }

    public void setResponseHeaders(Map<String, String> mResponseHeaders) {
        this.mResponseHeaders = mResponseHeaders;
    }

    public InputStream getContent() {
        return mInputStream;
    }

    public void setContent(InputStream mInputStream) {
        this.mInputStream = mInputStream;
    }

    public T getData()
    {
        return mData;
    }

    public void setData(T data) {
        this.mData = data;
    }

    public boolean isEmpty()
    {
        return mInputStream == null;
    }
}
