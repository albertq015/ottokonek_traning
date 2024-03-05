package com.otto.mart.model.APIModel.Response.topUpEmoney;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import app.beelabs.com.codebase.base.response.BaseResponse;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TopUpEmoneyProductResponseModel extends BaseResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        private List<Denomination> denomination;

        public List<Denomination> getDenomination() {
            return denomination;
        }

        public void setDenomination(List<Denomination> denomination) {
            this.denomination = denomination;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Denomination {

            private String product_name;
            private String product_code;
            private String logo;
            private boolean isSelected;

            public String getProduct_name() {
                return product_name;
            }

            public void setProduct_name(String product_name) {
                this.product_name = product_name;
            }

            public String getProduct_code() {
                return product_code;
            }

            public void setProduct_code(String product_code) {
                this.product_code = product_code;
            }

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public boolean isSelected() {
                return isSelected;
            }

            public void setSelected(boolean selected) {
                isSelected = selected;
            }
        }
    }
}
