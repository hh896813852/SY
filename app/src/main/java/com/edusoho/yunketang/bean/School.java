package com.edusoho.yunketang.bean;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class School {
    public String name;
    public String url;
    public String host;
    public String logo;
    public String version;
    public HashMap<String, String> apiVersionRange;
    public String[] splashs;

    public String getDomain() {
        try {
            URL url = new URL(host);
            return url.getHost();
        } catch (MalformedURLException e) {
        }

        return "";
    }
}