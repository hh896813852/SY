package com.edusoho.yunketang.http;


import android.text.TextUtils;

import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.bean.User;

/**
 * 与后台进行数据传入的工具
 * 一般用法:
 * SYDataTransport.create(URL)
 * .addParam(key1, value1)
 * .addParam(key2, value2)
 * .addDefaultProgressing()
 * .execute(DataListener, String.class);
 */
public class SYDataTransport extends DataTransport {
    protected SYDataTransport() {
    }

    public static SYDataTransport create(String url) {
        return create(url, true);
    }

    public static SYDataTransport create(String url, boolean needToken) {
        SYDataTransport dataChangeBiz = new SYDataTransport();
        dataChangeBiz.addUrl(url);
        if (needToken) {
            User loginUser = SYApplication.getInstance().getUser();
            if (loginUser != null) {
                if (url.startsWith(SYConstants.HTTP_URL_ONLINE)) {
                    if (!TextUtils.isEmpty(loginUser.syzxToken)) {
                        dataChangeBiz.addHead("X-Auth-Token", loginUser.syzxToken);
                    }
                } else if (url.startsWith(SYConstants.HTTP_URL_ACCOUNTANT)) {
                    if (!TextUtils.isEmpty(loginUser.sykjToken)) {
                        dataChangeBiz.addHead("X-Auth-Token", loginUser.sykjToken);
                    }
                } else {
                    if (!TextUtils.isEmpty(loginUser.syjyToken)) {
                        dataChangeBiz.addHead("token", loginUser.syjyToken);
                    }
                }
            }
        }
        return dataChangeBiz;
    }
}
