package com.otto.mart.model.APIModel.Response.ottopoint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpVoucherDealsResponseModel extends BaseResponseOttopoint {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data{

        private List<OpVoucherDealsDetailResponseModel.Data> campaigns;
        @JsonProperty("totalVoucher")
        private int total;
        @JsonProperty("totalPage")
        private int halaman;
        private long maximumCostInPoints;

        public List<OpVoucherDealsDetailResponseModel.Data> getCampaigns() {
            return campaigns;
        }

        public void setCampaigns(List<OpVoucherDealsDetailResponseModel.Data> campaigns) {
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

        public long getMaximumCostInPoints() {
            return maximumCostInPoints;
        }

        public void setMaximumCostInPoints(long maximumCostInPoints) {
            this.maximumCostInPoints = maximumCostInPoints;
        }
    }
}
