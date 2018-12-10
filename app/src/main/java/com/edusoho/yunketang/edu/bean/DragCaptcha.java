package com.edusoho.yunketang.edu.bean;

import java.io.Serializable;

public class DragCaptcha implements Serializable {

    private String token;
    private String jigsaw;
    private String url;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getJigsaw() {
        return jigsaw;
    }

    public void setJigsaw(String jigsaw) {
        this.jigsaw = jigsaw;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
