package com.otto.mart.model.APIModel.Response.ottopoint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpVoucherSayaResponseModel extends BaseResponseOttopoint {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data{

        private List<Campaigns> campaigns;
        @JsonProperty("totalVoucher")
        private int total;
        @JsonProperty("totalPage")
        private int halaman;

        public List<Campaigns> getCampaigns() {
            return campaigns;
        }

        public void setCampaigns(List<Campaigns> campaigns) {
            this.campaigns = campaigns;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getHalaman() {
            return halaman;
        }

        public void setHalaman(int halaman) {
            this.halaman = halaman;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Campaigns{
            private String name;
            private String brand_name;
            private boolean canBeUsed;
            private String purchaseAt;
            private long costInPoints;
            private String campaignId;
            private boolean used;
            private Coupon coupon;
            private String status;
            private String activeTo;
            private String deliveryStatus;
            private List<UrlPhoto> url_photo;
            private String url_logo;
            private String voucher_time;

            public boolean isCanBeUsed() {
                return canBeUsed;
            }

            public void setCanBeUsed(boolean canBeUsed) {
                this.canBeUsed = canBeUsed;
            }

            public String getPurchaseAt() {
                return purchaseAt;
            }

            public void setPurchaseAt(String purchaseAt) {
                this.purchaseAt = purchaseAt;
            }

            public long getCostInPoints() {
                return costInPoints;
            }

            public void setCostInPoints(long costInPoints) {
                this.costInPoints = costInPoints;
            }

            public String getCampaignId() {
                return campaignId;
            }

            public void setCampaignId(String campaignId) {
                this.campaignId = campaignId;
            }

            public boolean isUsed() {
                return used;
            }

            public void setUsed(boolean used) {
                this.used = used;
            }

            public Coupon getCoupon() {
                return coupon;
            }

            public void setCoupon(Coupon coupon) {
                this.coupon = coupon;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getActiveTo() {
                return activeTo;
            }

            public void setActiveTo(String activeTo) {
                this.activeTo = activeTo;
            }

            public String getDeliveryStatus() {
                return deliveryStatus;
            }

            public void setDeliveryStatus(String deliveryStatus) {
                this.deliveryStatus = deliveryStatus;
            }

            public List<UrlPhoto> getUrl_photo() {
                return url_photo;
            }

            public void setUrl_photo(List<UrlPhoto> url_photo) {
                this.url_photo = url_photo;
            }

            public String getUrl_logo() {
                return url_logo;
            }

            public void setUrl_logo(String url_logo) {
                this.url_logo = url_logo;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getBrand_name() {
                return brand_name;
            }

            public void setBrand_name(String brand_name) {
                this.brand_name = brand_name;
            }

            public String getVoucher_time() {
                return voucher_time;
            }

            public void setVoucher_time(String voucher_time) {
                this.voucher_time = voucher_time;
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Coupon{
                private String id;
                private String code;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }
            }
        }
    }
}
