package com.otto.mart.model.APIModel.Response.BaseModel;

import android.content.Intent;
import android.util.AndroidRuntimeException;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.OttoMartApp;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.ui.activity.login.LoginActivity;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MetaModel {
    int code;
    boolean status;
    String message;

    public int getCode() {
        if (code == 498) {
            try {
                SessionManager.logout();
                Intent intent = new Intent(OttoMartApp.getContext(), LoginActivity.class);
                intent.putExtra(LoginActivity.KEY_IS_SESSION_EXPIRED, true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                OttoMartApp.getContext().startActivity(intent);
            } catch (AndroidRuntimeException e) {
                e.printStackTrace();
            }
        }
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
