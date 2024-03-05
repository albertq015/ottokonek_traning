package com.otto.mart.model.APIModel.Response.topUpEmoney;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TopUpEmoneyDenomResponseModel extends BaseResponseModel {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        private Product product;
        private List<Denomination> denomination;

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

        public List<Denomination> getDenomination() {
            return denomination;
        }

        public void setDenomination(List<Denomination> denomination) {
            this.denomination = denomination;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Product {
            private String logo;

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Denomination {
            private String product_name;
            private String product_code;
            private String denom;
            private int price;
            private boolean disabled;

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

            public String getDenom() {
                return denom;
            }

            public void setDenom(String denom) {
                this.denom = denom;
            }

            public int getPrice() {
                return price;
            }

            public void setPrice(int price) {
                this.price = price;
            }

            public boolean isDisabled() {
                return disabled;
            }

            public void setDisabled(boolean disabled) {
                this.disabled = disabled;
            }
        }
    }
}
