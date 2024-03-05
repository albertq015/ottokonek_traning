package com.otto.mart.model.APIModel.Response.ppob;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PpobInquiryResponse extends BaseResponseModel {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {

        private int admin_fee;
        private long amount;
        private String cust_id;
        private ListDetail list_detail;
        private List<WidgetBuilderModel> key_value_list;
        private String msg;
        private String product_code;
        private String rc;
        private String rrn;
        private String total;
        private String cashback_omzet;

        public int getAdmin_fee() {
            return admin_fee;
        }

        public void setAdmin_fee(int admin_fee) {
            this.admin_fee = admin_fee;
        }

        public long getAmount() {
            return amount;
        }

        public void setAmount(long amount) {
            this.amount = amount;
        }

        public String getCust_id() {
            return cust_id;
        }

        public void setCust_id(String cust_id) {
            this.cust_id = cust_id;
        }

        public ListDetail getList_detail() {
            return list_detail;
        }

        public void setList_detail(ListDetail list_detail) {
            this.list_detail = list_detail;
        }

        public List<WidgetBuilderModel> getKey_value_list() {
            return key_value_list;
        }

        public void setKey_value_list(List<WidgetBuilderModel> key_value_list) {
            this.key_value_list = key_value_list;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getProduct_code() {
            return product_code;
        }

        public void setProduct_code(String product_code) {
            this.product_code = product_code;
        }

        public String getRc() {
            return rc;
        }

        public void setRc(String rc) {
            this.rc = rc;
        }

        public String getRrn() {
            return rrn;
        }

        public void setRrn(String rrn) {
            this.rrn = rrn;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getCashback_omzet() {
            return cashback_omzet;
        }

        public void setCashback_omzet(String cashback_omzet) {
            this.cashback_omzet = cashback_omzet;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ListDetail {

            @JsonProperty("Company")
            private String Company;
            @JsonProperty("Denom")
            private String Denom;
            @JsonProperty("Name")
            private String Name;
            @JsonProperty("ProductCode")
            private String ProductCode;
            @JsonProperty("SalesPrice")
            private String SalesPrice;
            private String custname;
            private String amount;
            private String meterNo;
            private String power;

            public String getCompany() {
                return Company;
            }

            public void setCompany(String Company) {
                this.Company = Company;
            }

            public String getDenom() {
                return Denom;
            }

            public void setDenom(String Denom) {
                this.Denom = Denom;
            }

            public String getName() {
                return Name;
            }

            public void setName(String Name) {
                this.Name = Name;
            }

            public String getProductCode() {
                return ProductCode;
            }

            public void setProductCode(String ProductCode) {
                this.ProductCode = ProductCode;
            }

            public String getSalesPrice() {
                return SalesPrice;
            }

            public void setSalesPrice(String SalesPrice) {
                this.SalesPrice = SalesPrice;
            }

            public String getCustname() {
                return custname;
            }

            public void setCustname(String custname) {
                this.custname = custname;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getMeterNo() {
                return meterNo;
            }

            public void setMeterNo(String meterNo) {
                this.meterNo = meterNo;
            }

            public String getPower() {
                return power;
            }

            public void setPower(String power) {
                this.power = power;
            }
        }
    }
}
