package com.otto.mart.model.APIModel.Response.donasi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDonasiResponse extends BaseResponseModel {

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
            private String images;
            private String operator;
            private List<Detail> detail;

            public String getImages() {
                return images;
            }

            public void setImages(String images) {
                this.images = images;
            }

            public String getOperator() {
                return operator;
            }

            public void setOperator(String operator) {
                this.operator = operator;
            }

            public List<Detail> getDetail() {
                return detail;
            }

            public void setDetail(List<Detail> detail) {
                this.detail = detail;
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Detail {
                private String desc;
                private String name;
                private String productcode;

                public String getDesc() {
                    return desc;
                }

                public void setDesc(String desc) {
                    this.desc = desc;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getProductcode() {
                    return productcode;
                }

                public void setProductcode(String productcode) {
                    this.productcode = productcode;
                }
            }
        }
    }
}
