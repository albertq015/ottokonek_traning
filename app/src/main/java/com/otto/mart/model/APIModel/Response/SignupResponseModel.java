package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SignupResponseModel extends BaseResponseModel {
    private SignupDataModel data;

    public SignupDataModel getData() {
        return data;
    }

    public void setData(SignupDataModel data) {
        this.data = data;
    }

    public int getUser_id() {
        return data.user_id;
    }

    public void setUser_id(int user_id) {
        data.user_id = user_id;
    }

    public String getName() {
        return data.name;
    }

    public void setName(String name) {
        data.name = name;
    }

    public String getMerchant_name() {
        return data.merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        data.merchant_name = merchant_name;
    }

    public String getPhone() {
        return data.phone;
    }

    public void setPhone(String phone) {
        data.phone = phone;
    }

    public String getStatus() {
        return data.status;
    }

    public void setStatus(String status) {
        data.status = status;
    }

    public String getRequestId() {
        return data.getRequest_linking_id();
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class SignupDataModel {

    int user_id;
    String name;
    String merchant_name;
    String phone;
    String status;
    String request_linking_id;

    public String getRequest_linking_id() {
        return request_linking_id;
    }

    public void setRequest_linking_id(String request_linking_id) {
        this.request_linking_id = request_linking_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
