package com.edusoho.yunketang.edu.request;


import com.edusoho.yunketang.edu.request.model.Response;

/**
 * Created by howzhi on 15/4/28.
 */
public interface RequestCallback<T> {

    T onResponse(Response<T> response);
}
