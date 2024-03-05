package com.otto.mart.model.APIModel.Response.ppob;

import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

public class PpobUserGuideResponse extends BaseResponseModel {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {

        private String operator;
        private String user_guide;

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public String getUser_guide() {
            return user_guide;
        }

        public void setUser_guide(String user_guide) {
            this.user_guide = user_guide;
        }
    }
}
