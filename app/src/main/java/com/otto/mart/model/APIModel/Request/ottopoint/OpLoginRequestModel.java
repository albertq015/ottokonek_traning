package com.otto.mart.model.APIModel.Request.ottopoint;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OpLoginRequestModel {

    private String _username;
    private String _password;

    @JsonProperty("<user_type>")
    private String user_type;

    public OpLoginRequestModel(String _username, String _password, String user_type){
        this._username = _username;
        this._password = _password;
        this.user_type = user_type;
    }

    public String get_username() {
        return _username;
    }

    public void set_username(String _username) {
        this._username = _username;
    }

    public String get_password() {
        return _password;
    }

    public void set_password(String _password) {
        this._password = _password;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
