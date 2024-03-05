package com.otto.mart.model.APIModel.Response.merchant.theme;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MerchantTheme {
    private String dashboardLogo;
    private String dashboardText;
    private String dashboardTopBackground;
    private int id;
    private int userCategoryId;
    private String levelMerchantCodeId;
    private String userCategoryName;
    private String levelMerchantName;
    private String profileBackgroundImage;
    private String levelMerchantId;
    private String themeColor;
    private String status;
    private Object banner;

    public String getDashboardLogo() {
        return dashboardLogo;
    }

    public void setDashboardLogo(String dashboardLogo) {
        this.dashboardLogo = dashboardLogo;
    }

    public String getDashboardText() {
        return dashboardText;
    }

    public void setDashboardText(String dashboardText) {
        this.dashboardText = dashboardText;
    }

    public String getDashboardTopBackground() {
        return dashboardTopBackground;
    }

    public void setDashboardTopBackground(String dashboardTopBackground) {
        this.dashboardTopBackground = dashboardTopBackground;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserCategoryId() {
        return userCategoryId;
    }

    public void setUserCategoryId(int userCategoryId) {
        this.userCategoryId = userCategoryId;
    }

    public String getLevelMerchantCodeId() {
        return levelMerchantCodeId;
    }

    public void setLevelMerchantCodeId(String levelMerchantCodeId) {
        this.levelMerchantCodeId = levelMerchantCodeId;
    }

    public String getUserCategoryName() {
        return userCategoryName;
    }

    public void setUserCategoryName(String userCategoryName) {
        this.userCategoryName = userCategoryName;
    }

    public String getLevelMerchantName() {
        return levelMerchantName;
    }

    public void setLevelMerchantName(String levelMerchantName) {
        this.levelMerchantName = levelMerchantName;
    }

    public String getProfileBackgroundImage() {
        return profileBackgroundImage;
    }

    public void setProfileBackgroundImage(String profileBackgroundImage) {
        this.profileBackgroundImage = profileBackgroundImage;
    }

    public String getLevelMerchantId() {
        return levelMerchantId;
    }

    public void setLevelMerchantId(String levelMerchantId) {
        this.levelMerchantId = levelMerchantId;
    }

    public String getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(String themeColor) {
        this.themeColor = themeColor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getBanner() {
        return banner;
    }

    public void setBanner(Object banner) {
        this.banner = banner;
    }
}
