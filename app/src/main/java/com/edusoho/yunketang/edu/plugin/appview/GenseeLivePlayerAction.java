package com.edusoho.yunketang.edu.plugin.appview;

import android.app.Activity;
import android.text.TextUtils;

import com.gensee.common.ServiceType;
import com.gensee.entity.InitParam;
import com.gensee.fastsdk.GenseeLive;
import com.gensee.fastsdk.GenseeVod;
import com.gensee.fastsdk.core.GSFastConfig;

import java.util.HashMap;

/**
 * Created by 菊 on 2016/4/11.
 */
public class GenseeLivePlayerAction {

    private Activity mActivity;

    public GenseeLivePlayerAction(Activity activity) {
        this.mActivity = activity;
    }

    public void invoke(HashMap<String, String> liveParams) {
        GSFastConfig gsFastConfig = new GSFastConfig();
        InitParam initParam = new InitParam();
        String domain = liveParams.get("domain");
        String number = liveParams.get("roomNumber");
        String loginAccount = liveParams.get("loginAccount");
        String loginPwd = liveParams.get("loginPwd");
        String vodPwd = liveParams.get("vodPwd");
        String joinPwd = liveParams.get("joinPwd");
        String nickName = liveParams.get("nickName");
        String serviceType = liveParams.get("serviceType");
        String k = liveParams.get("k");
        boolean playType = Boolean.valueOf(liveParams.get("replayState"));

        initParam.setDomain(domain);
        initParam.setNumber(number);
        initParam.setLoginAccount(loginAccount);
        initParam.setLoginPwd(loginPwd);
        initParam.setNickName(nickName);
        initParam.setK(k);
        if (!TextUtils.isEmpty(number) && number.length() == 8) {
            initParam.setNumber(number);
        } else {
            initParam.setNumber(number);
            initParam.setLiveId(number);
        }
        if (!TextUtils.isEmpty(vodPwd)) {
            initParam.setVodPwd(vodPwd);
        } else {
            initParam.setJoinPwd(joinPwd);
        }

        if (!TextUtils.isEmpty(serviceType) && serviceType.equals(ServiceType.WEBCAST.getValue())) {
            initParam.setServiceType(ServiceType.WEBCAST);
        } else {
            initParam.setServiceType(ServiceType.TRAINING);
        }

        if (playType) {
            GenseeVod.startVod(mActivity, initParam);
        } else {
            GenseeLive.startLive(mActivity, gsFastConfig, initParam);
        }
    }
}
