package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactUsResponse extends BaseResponseModel {
    private ContactUsData data;

    public ContactUsData getData() {
        return data;
    }

    public void setData(ContactUsData data) {
        this.data = data;
    }

    public class ContactUsData {
        private String whatsapp;
        private String website;
        private String telepon;
        private String support_hours;
        private String email;

        public void setWhatsapp(String whatsapp) {
            this.whatsapp = whatsapp;
        }

        public String getWhatsapp() {
            return whatsapp;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getWebsite() {
            return website;
        }

        public void setTelepon(String telepon) {
            this.telepon = telepon;
        }

        public String getTelepon() {
            return telepon;
        }

        public String getSupport_hours() {
            return support_hours;
        }

        public void setSupport_hours(String support_hours) {
            this.support_hours = support_hours;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }
    }
}
