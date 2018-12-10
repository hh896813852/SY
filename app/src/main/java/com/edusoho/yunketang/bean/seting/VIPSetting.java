package com.edusoho.yunketang.bean.seting;


public class VIPSetting {

    private boolean enabled;
    private String  buyType;
    private int     upgradeMinDay;
    private String  defaultBuyYears;
    private String  defaultBuyMonths;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getBuyType() {
        return buyType;
    }

    public void setBuyType(String buyType) {
        this.buyType = buyType;
    }

    public int getUpgradeMinDay() {
        return upgradeMinDay;
    }

    public void setUpgradeMinDay(int upgradeMinDay) {
        this.upgradeMinDay = upgradeMinDay;
    }

    public String getDefaultBuyYears() {
        return defaultBuyYears;
    }

    public void setDefaultBuyYears(String defaultBuyYears) {
        this.defaultBuyYears = defaultBuyYears;
    }

    public String getDefaultBuyMonths() {
        return defaultBuyMonths;
    }

    public void setDefaultBuyMonths(String defaultBuyMonths) {
        this.defaultBuyMonths = defaultBuyMonths;
    }
}
