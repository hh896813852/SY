package com.edusoho.yunketang.bean;


import com.edusoho.yunketang.bean.json.GsonEnum;

/**
 * Created by howzhi on 14-8-19.
 */

public enum UserRole implements GsonEnum<UserRole> {
    ROLE_USER("学员"), ROLE_SUPER_ADMIN("超管"), ROLE_TEACHER("教师"), ROLE_ADMIN("管理员"), NO_SUPPORT("");

    String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    @Override
    public UserRole deserialize(String jsonEnum) {
        switch (jsonEnum) {
            case "ROLE_ADMIN":
            case "ROLE_SUPER_ADMIN":
            case "ROLE_TEACHER":
            case "ROLE_USER":
                return valueOf(jsonEnum);
        }
        return NO_SUPPORT;
    }

    @Override
    public String serialize() {
        return name();
    }
}
