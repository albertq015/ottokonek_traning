package com.otto.mart.model.APIModel.Misc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.voucherGame.VoucherGameData;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OttoagDenomModel {
    private String provider_code;
    private int denomination;
    private int price;
    private int fee;
    private int promo;
    private String product_code;
    private String biller_code;
    private String description;
    private String product_name;
    private String logo;
    private Long cashback_omzet;
    private boolean disabled;
    private boolean selected;
    private boolean direct;
    private String remarks;
    private String remark_title;
    private List<String> field_input;
    private List<OptionField> option_field;

    public String getProvider_code() {
        return provider_code;
    }

    public void setProvider_code(String provider_code) {
        this.provider_code = provider_code;
    }

    public int getDenomination() {
        return denomination;
    }

    public void setDenomination(int denomination) {
        this.denomination = denomination;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getPromo() {
        return promo;
    }

    public void setPromo(int promo) {
        this.promo = promo;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getBiller_code() {
        return biller_code;
    }

    public void setBiller_code(String biller_code) {
        this.biller_code = biller_code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Long getCashback_omzet() {
        return cashback_omzet;
    }

    public void setCashback_omzet(Long cashback_omzet) {
        this.cashback_omzet = cashback_omzet;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isDirect() {
        return direct;
    }

    public void setDirect(boolean direct) {
        this.direct = direct;
    }

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

    public List<String> getField_input() {
        return field_input;
    }

    public void setField_input(List<String> field_input) {
        this.field_input = field_input;
    }

    public List<OptionField> getOption_field() {
        return option_field;
    }

    public void setOption_field(List<OptionField> option_field) {
        this.option_field = option_field;
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OptionField {
        private String field_name;
        private List<Serverid> data;

        public String getField_name() {
            return field_name;
        }

        public void setField_name(String field_name) {
            this.field_name = field_name;
        }

        public List<Serverid> getData() {
            return data;
        }

        public void setData(List<Serverid> data) {
            this.data = data;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Serverid {
            private String backend_value;
            private String display_name;

            public String getBackend_value() {
                return backend_value;
            }

            public void setBackend_value(String backend_value) {
                this.backend_value = backend_value;
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
