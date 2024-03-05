package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.BannerModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BannerRespomnse extends BaseResponseModel {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        private List<BannerModel> banner;
        private String dashboardLogo;
        private String profileBackgroundImage;

        public String getDashboardLogo() {
            return dashboardLogo;
        }

        public void setDashboardLogo(String dashboardLogo) {
            this.dashboardLogo = dashboardLogo;
        }

        public String getProfileBackgroundImage() {
            return profileBackgroundImage;
        }

        public void setProfileBackgroundImage(String profileBackgroundImage) {
            this.profileBackgroundImage = profileBackgroundImage;
        }

        public List<BannerModel> getBanner() {
            return banner;
        }

        public void setBanner(List<BannerModel> banner) {
            this.banner = banner;
        }
    }
}
