package com.edusoho.yunketang.edu.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.edusoho.baijiayun.ChooseActivity;
import com.edusoho.yunketang.edu.bean.CourseTask;
import com.edusoho.yunketang.edu.plugin.appview.GenseeLivePlayerAction;
import com.google.gson.JsonObject;

import java.util.HashMap;

public class LiveTaskLauncher {
    private Context mContext;
    private JsonObject mLiveTick;
    private CourseTask mCourseTask;

    private LiveTaskLauncher(Builder builder) {
        this.mContext = builder.mContext;
        this.mLiveTick = builder.mLiveTick;
        this.mCourseTask = builder.mCourseTask;
    }

    public static Builder build() {
        return new Builder();
    }

    public void launch() {
        HashMap<String, String> liveParams = wrapperLiveTicket(mLiveTick);

        if (liveParams != null) {
            switch (liveParams.get("provider")) {
//                case Constants.LiveProvider.LONGINUS:
//                    new LonginusLivePlayerAction((Activity) mContext).invoke(liveParams, mCourseTask.id, mCourseTask.title);
//                    break;
//                case Constants.LiveProvider.APOLLO:
//                    new AthenaLivePlayerAction((Activity) mContext).invoke(liveParams, mCourseTask);
//                    break;
                case Constants.LiveProvider.GENSEE:
                    new GenseeLivePlayerAction((Activity) mContext).invoke(liveParams);
                    break;
//                case Constants.LiveProvider.TALKFUN:
//                    Bundle talkfunBundle = new Bundle();
//                    talkfunBundle.putBoolean("replayState", Boolean.valueOf(liveParams.get("replayState")));
//                    talkfunBundle.putString("token", liveParams.get("token"));
//                    new TalkFunLivePlayerAction((Activity) mContext).invoke(talkfunBundle);
//                    break;
                case Constants.LiveProvider.BAIJIAYUN:
                    Bundle baijiayunBundle = new Bundle();
                    baijiayunBundle.putBoolean("replayState", Boolean.valueOf(liveParams.get("replayState")));
                    baijiayunBundle.putString("roomId", liveParams.get("roomId"));
                    baijiayunBundle.putString("userNumber", liveParams.get("userNumber"));
                    baijiayunBundle.putString("userName", liveParams.get("userName"));
                    baijiayunBundle.putString("userType", liveParams.get("userType"));
                    baijiayunBundle.putString("userAvatar", liveParams.get("userAvatar"));
                    baijiayunBundle.putString("sign", liveParams.get("sign"));
                    baijiayunBundle.putString("classId", liveParams.get("classId"));
                    baijiayunBundle.putString("token", liveParams.get("token"));

                    Intent intent = new Intent(mContext, ChooseActivity.class);
                    intent.putExtras(baijiayunBundle);
                    mContext.startActivity(intent);
                    break;
                default:
                    ToastUtils.show(mContext, "暂不支持该直播类型在客户端上播放");
            }
        } else if (mLiveTick.get("url") != null && !StringUtils.isEmpty(mLiveTick.get("url").getAsString())) {
//            NewWebViewActivity.launch(mContext, mLiveTick.get("url").getAsString());
        }
    }

    private HashMap<String, String> wrapperLiveTicket(JsonObject liveTick) {
        HashMap<String, String> liveParams = null;
        JsonObject extra = liveTick.getAsJsonObject("extra");
        JsonObject user = liveTick.getAsJsonObject("user");
        if (extra != null && extra.get("provider") != null) {
            liveParams = new HashMap<>();
            String liveType = extra.get("provider").getAsString();
            liveParams.put("provider", liveType);
            switch (liveType) {
                case Constants.LiveProvider.LONGINUS:
                    //回放
                    liveParams.put("url", liveTick.get("url").getAsString());

                    liveParams.put("token", extra.get("token").getAsString());
                    liveParams.put("nickname", extra.get("nickname").getAsString());
                    liveParams.put("convNo", extra.get("convNo").getAsString());
                    liveParams.put("roomNo", extra.get("roomNo").getAsString());
                    liveParams.put("clientId", extra.get("clientId").getAsString());
                    liveParams.put("requestUrl", extra.get("requestUrl").getAsString());

                    JsonObject play = extra.getAsJsonObject("play");
                    liveParams.put("playUrl", play.get("url").getAsString() + play.get("stream").getAsString());
                    break;
                case Constants.LiveProvider.APOLLO:
                    liveParams.put("id", user.get("id").getAsString());
                    liveParams.put("role", extra.get("role").getAsString());
                    liveParams.put("nickname", extra.get("nickname").getAsString());
                    liveParams.put("token", extra.get("token").getAsString());
                    liveParams.put("httpUrl", extra.get("httpUrl").getAsString());
                    liveParams.put("sslUrl", extra.get("sslUrl").getAsString());
                    break;
                case Constants.LiveProvider.TALKFUN:
                    liveParams.put("replayState", liveTick.get("replayState").getAsBoolean() + "");
                    liveParams.put("token", extra.get("access_token").getAsString());
                    break;
                case Constants.LiveProvider.GENSEE:
                    liveParams.put("replayState", liveTick.get("replayState").getAsBoolean() + "");
                    liveParams.put("domain", extra.get("domain").getAsString());
                    liveParams.put("roomNumber", extra.get("roomNumber").getAsString());
                    liveParams.put("loginAccount", extra.get("loginAccount").getAsString());
                    liveParams.put("loginPwd", extra.get("loginPwd").getAsString());
                    liveParams.put("nickName", extra.get("nickName").getAsString());
                    liveParams.put("serviceType", extra.get("serviceType").getAsString());
                    liveParams.put("k", extra.get("k").getAsString());
                    if (liveTick.get("replayState").getAsBoolean()) {
                        liveParams.put("vodPwd", extra.get("vodPwd").getAsString());
                    } else {
                        liveParams.put("joinPwd", extra.get("joinPwd").getAsString());
                    }
                    break;
                case Constants.LiveProvider.BAIJIAYUN:
                    liveParams.put("replayState", liveTick.get("replayState").getAsBoolean() + "");
                    if (liveTick.get("replayState").getAsBoolean()) {
                        liveParams.put("classId", extra.get("classId").getAsString());
                        liveParams.put("token", extra.get("token").getAsString());
                    } else {
                        liveParams.put("roomId", extra.get("roomId").getAsString());
                        liveParams.put("userNumber", extra.get("userNumber").getAsString());
                        liveParams.put("userType", extra.get("userType").getAsString());
                        liveParams.put("userName", extra.get("userName").getAsString());
                        liveParams.put("userAvatar", extra.get("userAvatar").getAsString());
                        liveParams.put("sign", extra.get("sign").getAsString());
                    }
                    break;
            }
        }
        return liveParams;
    }

    public static class Builder {
        private Context mContext;
        private JsonObject mLiveTick;
        private CourseTask mCourseTask;

        public Builder init(Context context) {
            this.mContext = context;
            return this;
        }

        public Builder setLiveTick(JsonObject liveTick) {
            this.mLiveTick = liveTick;
            return this;
        }

        public Builder setCourseTask(CourseTask courseTask) {
            this.mCourseTask = courseTask;
            return this;
        }

        public LiveTaskLauncher build() {
            return new LiveTaskLauncher(this);
        }
    }
}
