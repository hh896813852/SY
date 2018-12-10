package com.edusoho.yunketang.base;

public class MainTabItem {
    public String title;
    public int imgSrcSelected;
    public int imgSrcNormal;

    public MainTabItem(String title, int imgSrcSelected, int imgSrcNormal) {
        this.title = title;
        this.imgSrcSelected = imgSrcSelected;
        this.imgSrcNormal = imgSrcNormal;
    }
}