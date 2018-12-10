package com.edusoho.yunketang.bean.seting;


import java.io.Serializable;

public class UserSetting implements Serializable {

    public String register_mode;
    public String oauth_enabled;
    public String weibo_enabled;
    public String qq_enabled;
    public String renren_enabled;
    public String weixinweb_enabled;
    public String weixinmob_enabled;

    public boolean isRegisterEanble() {
        return "mobile".equals(register_mode) || "email_or_mobile".equals(register_mode);
    }

    public boolean isOAuthLoginEnable() {
        return "1".equals(oauth_enabled);
    }

    public boolean isQQLoginEnable() {
        return "1".equals(qq_enabled);
    }

    public boolean isWeiboLoginEnable() {
        return "1".equals(weibo_enabled);
    }

    public boolean isWeixinLoginEnable() {
        return "1".equals(weixinmob_enabled);
    }

    public void init() {
        this.register_mode = "email_or_mobile";
        this.oauth_enabled = "1";
        this.oauth_enabled = "1";
        this.weibo_enabled = "1";
        this.qq_enabled = "1";
        this.renren_enabled = "1";
        this.weixinweb_enabled = "1";
        this.weixinmob_enabled = "1";
    }

}
