package com.otto.mart.model.APIModel.Misc.voucherGame;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VoucherGameData {
    private String remarks;
    private String remark_title;
    public String product_name;
    public String product_code;
    private String logo;
    private boolean direct;
    private OptionField option_field;
    private List<String> field_input;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemark_title() {
        return remark_title;
    }

    public void setRemark_title(String remark_title) {
        this.remark_title = remark_title;
    }

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

    public boolean isDirect() {
        return direct;
    }

    public void setDirect(boolean direct) {
        this.direct = direct;
    }

    public OptionField getOption_field() {
        return option_field;
    }

    public void setOption_field(OptionField option_field) {
        this.option_field = option_field;
    }

    public List<String> getField_input() {
        return field_input;
    }

    public void setField_input(List<String> field_input) {
        this.field_input = field_input;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OptionField {
        private List<Serverid> serverid;

        public List<Serverid> getServerid() {
            return serverid;
        }

        public void setServerid(List<Serverid> serverid) {
            this.serverid = serverid;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Serverid {
            private String value;
            private String display_name;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getDisplay_name() {
                return display_name;
            }

            public void setDisplay_name(String display_name) {
                this.display_name = display_name;
            }
        }
    }
}
