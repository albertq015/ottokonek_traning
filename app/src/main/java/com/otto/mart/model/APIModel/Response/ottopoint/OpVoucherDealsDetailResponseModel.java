package com.otto.mart.model.APIModel.Response.ottopoint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpVoucherDealsDetailResponseModel extends BaseResponseOttopoint implements Serializable {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        private String name;
        private boolean brandIcon;
        private String brandName;
        private String campaignId;
        private String reward;
        private boolean active;
        private int costInPoints;
        private boolean singleCoupon;
        private boolean unlimited;
        private int limit;
        private int limitPerUser;
        private List<Labels> labels;
        private boolean featured;

        @JsonProperty("public")
        private boolean keyPublic;
        private boolean fulfillmentTracking;

        @Expose
        private Object categoryNames;
        private long usageLeft;
        private int usageLeftForCustomer;
        private boolean canBeBoughtByCustomer;
        private String brandDescription;
        private String shortDescription;
        private String conditionsDescription;
        private String usageInstruction;
        private String url_logo;
        private List<UrlPhoto> url_photo;
        private List<String> coupons;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isBrandIcon() {
            return brandIcon;
        }

        public void setBrandIcon(boolean brandIcon) {
            this.brandIcon = brandIcon;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public String getCampaignId() {
            return campaignId;
        }

        public void setCampaignId(String campaignId) {
            this.campaignId = campaignId;
        }

        public String getReward() {
            return reward;
        }

        public void setReward(String reward) {
            this.reward = reward;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public int getCostInPoints() {
            return costInPoints;
        }

        public void setCostInPoints(int costInPoints) {
            this.costInPoints = costInPoints;
        }

        public boolean isSingleCoupon() {
            return singleCoupon;
        }

        public void setSingleCoupon(boolean singleCoupon) {
            this.singleCoupon = singleCoupon;
        }

        public boolean isUnlimited() {
            return unlimited;
        }

        public void setUnlimited(boolean unlimited) {
            this.unlimited = unlimited;
        }

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public int getLimitPerUser() {
            return limitPerUser;
        }

        public void setLimitPerUser(int limitPerUser) {
            this.limitPerUser = limitPerUser;
        }

        public List<Labels> getLabels() {
            return labels;
        }

        public void setLabels(List<Labels> labels) {
            this.labels = labels;
        }

        public boolean isFeatured() {
            return featured;
        }

        public void setFeatured(boolean featured) {
            this.featured = featured;
        }

        public boolean isKeyPublic() {
            return keyPublic;
        }

        public void setKeyPublic(boolean keyPublic) {
            this.keyPublic = keyPublic;
        }

        public boolean isFulfillmentTracking() {
            return fulfillmentTracking;
        }

        public void setFulfillmentTracking(boolean fulfillmentTracking) {
            this.fulfillmentTracking = fulfillmentTracking;
        }

        public long getUsageLeft() {
            return usageLeft;
        }

        public void setUsageLeft(long usageLeft) {
            this.usageLeft = usageLeft;
        }

        public int getUsageLeftForCustomer() {
            return usageLeftForCustomer;
        }

        public void setUsageLeftForCustomer(int usageLeftForCustomer) {
            this.usageLeftForCustomer = usageLeftForCustomer;
        }

        public boolean isCanBeBoughtByCustomer() {
            return canBeBoughtByCustomer;
        }

        public void setCanBeBoughtByCustomer(boolean canBeBoughtByCustomer) {
            this.canBeBoughtByCustomer = canBeBoughtByCustomer;
        }

        public String getBrandDescription() {
            return brandDescription;
        }

        public void setBrandDescription(String brandDescription) {
            this.brandDescription = brandDescription;
        }

        public String getShortDescription() {
            return shortDescription;
        }

        public void setShortDescription(String shortDescription) {
            this.shortDescription = shortDescription;
        }

        public String getConditionsDescription() {
            return conditionsDescription;
        }

        public void setConditionsDescription(String conditionsDescription) {
            this.conditionsDescription = conditionsDescription;
        }

        public String getUsageInstruction() {
            return usageInstruction;
        }

        public void setUsageInstruction(String usageInstruction) {
            this.usageInstruction = usageInstruction;
        }

        public String getUrl_logo() {
            return url_logo;
        }

        public void setUrl_logo(String url_logo) {
            this.url_logo = url_logo;
        }

        public List<UrlPhoto> getUrl_photo() {
            return url_photo;
        }

        public void setUrl_photo(List<UrlPhoto> url_photo) {
            this.url_photo = url_photo;
        }

        public Object getCategoryNames() {
            return categoryNames;
        }

        public void setCategoryNames(Object categoryNames) {
            this.categoryNames = categoryNames;
        }

        public List<String> getCoupons() {
            return coupons;
        }

        public void setCoupons(List<String> coupons) {
            this.coupons = coupons;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Labels {
            private String key;
            private String value;

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }
}
