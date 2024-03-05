package com.otto.mart.model.APIModel.Response;

import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

public class ForgotPinSecurityQuestionResponse extends BaseResponseModel {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private String securityQuestion;
        private int securityQuestionId;

        public String getSecurityQuestion() {
            return securityQuestion;
        }

        public void setSecurityQuestion(String securityQuestion) {
            this.securityQuestion = securityQuestion;
        }

        public int getSecurityQuestionId() {
            return securityQuestionId;
        }

        public void setSecurityQuestionId(int securityQuestionId) {
            this.securityQuestionId = securityQuestionId;
        }
    }
}
