package com.edusoho.yunketang.bean;

import com.google.gson.annotations.SerializedName;
import com.edusoho.yunketang.edu.bean.SimpleVip;
import com.edusoho.yunketang.edu.bean.innerbean.Avatar;

import java.io.Serializable;

/**
 * @author huhao on 2018/11/19
 */
public class User implements Serializable {
    public String     mobile;
    public String     nickname;
    public String     email;
    public String     password;
    public String     id;
    public UserRole[] roles;
    public String     uri;
    public String     title;
    public String     type;
    public String     about;
    public String     role;
    public String     mediumAvatar;
    public Avatar     avatar;
    @SerializedName("syjy_token")
    public String     syjyToken;
    @SerializedName("syzx_token")
    public String     syzxToken;
    @SerializedName("sykj_token")
    public String     sykjToken;
    public String     token;
    public String     uuid;
    public String     userId;
    @SerializedName("syjy_user")
    public User syjyUser;
    public String syzx_user;
    public String sykj_user;
    public User syzxUser;
    public User sykjUser;

    public User getOtherUser() {
        return syzxUser != null ? syzxUser : sykjUser;
    }

    public User getUser() {
        return syjyUser;
    }

    /**
     * 关注
     */
    public String following;
    /**
     * 粉丝
     */
    public String follower;

    public Vip vip;

    public String thirdParty;

    public String getAvatar() {
        if (avatar != null) {
            return avatar.middle;
        } else {
            return mediumAvatar;
        }
    }

    public String userRole2String() {
        if (roles == null || roles.length == 0) {
            return "学员";
        }

        StringBuilder sb = new StringBuilder();
        for (UserRole userRole : roles) {
            if (userRole != null) {
                sb.append(userRole.getRoleName()).append(" ");
            }
        }

        return sb.toString();
    }

    public void setVip(SimpleVip simpleVip) {
        if (simpleVip == null) {
            this.vip = null;
            return;
        }
        if (vip == null) {
            vip = new Vip();
        }
        vip.id = simpleVip.getLevelId();
        vip.name = simpleVip.getVipName();
        vip.deadline = simpleVip.getDeadline();
        vip.seq = simpleVip.getSeq();
    }

    public SimpleVip getVip() {
        if (vip == null) {
            return null;
        }
        return convertSimpleVip(vip);
    }

    public boolean isVIP() {
        return vip != null;
    }

    private SimpleVip convertSimpleVip(Vip vip) {
        SimpleVip simpleVip = new SimpleVip();
        simpleVip.setLevelId(vip.id);
        simpleVip.setVipName(vip.name);
        simpleVip.setSeq(vip.seq);
        simpleVip.setDeadline(vip.deadline);
        return simpleVip;
    }
}
