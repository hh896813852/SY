package com.edusoho.yunketang.edu.webview.bridgeadapter.bridge;

/**
 * Created by howzhi on 15/4/17.
 */
public class CallbackStatus<T> {

    public static final int SUCCESS = 0;
    public static final int ERROR = -1;
    public static final int ASYN = 1;

    protected int status;
    protected T message;

    public void setSuccess(T message)
    {
        this.status = SUCCESS;
        this.message = message;
    }

    public void setAsyn()
    {
        this.status = ASYN;
    }

    public void setError(T message)
    {
        this.status = ERROR;
        this.message = message;
    }

    public int getStatus()
    {
        return status;
    }

    public T getMessage()
    {
        return message;
    }
}
