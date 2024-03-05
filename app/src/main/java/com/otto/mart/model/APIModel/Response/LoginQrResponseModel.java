package com.otto.mart.model.APIModel.Response;

import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;


public class LoginQrResponseModel extends BaseResponseModel {

    private LoginQrResponseData data;

    public LoginQrResponseData getData() {
        return data;
    }

    public void setData(LoginQrResponseData data) {
        this.data = data;
    }

    public String getPhone() {
        return data.phone;
    }

    public void setPhone(String phone) {
        data.phone = phone;
    }

    public class LoginQrResponseData {
        private String phone;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}


