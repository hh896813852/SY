package com.edusoho.yunketang.edu.request;


import com.edusoho.yunketang.edu.request.model.Request;
import com.edusoho.yunketang.edu.request.model.Response;

/**
 * Created by howzhi on 15/4/28.
 */
public interface RequestHandler {

    void handler(Request request, Response response);
}
