package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserSecurityQuestionResponseModel extends BaseResponseModel {
    UserSecurityQuestionData data;

    public UserSecurityQuestionData getData() {
        return data;
    }

    public void setData(UserSecurityQuestionData data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserSecurityQuestionData {
        KMBResponse kmb_response;
        Question question;

        public KMBResponse getKmb_response() {
            return kmb_response;
        }

        public void setKmb_response(KMBResponse kmb_response) {
            this.kmb_response = kmb_response;
        }

        public Question getQuestion() {
            return question;
        }

        public void setQuestion(Question question) {
            this.question = question;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KMBResponse {
        int securityQuestionId;

        public int getSecurityQuestionId() {
            return securityQuestionId;
        }

        public void setSecurityQuestionId(int securityQuestionId) {
            this.securityQuestionId = securityQuestionId;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Question {
        int id;
        String question;
        String created_at;
        String updated_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }
}

//    UserSecurityQuestionData data;
//
//    public UserSecurityQuestionData getData() {
//        return data;
//    }
//
//    public void setData(UserSecurityQuestionData data) {
//        this.data = data;
//    }
//
//    public int getSecurity_question_id() {
//        return data.security_question_id;
//    }
//
//    public void setSecurity_question_id(int security_question_id) {
//        data.security_question_id = security_question_id;
//    }
//
//    public String getPhone() {
//        return data.phone;
//    }
//
//    public void setPhone(String phone) {
//        data.phone = phone;
//    }
//
//    public String getQuestion_description() {
//        return data.question_description;
//    }
//
//    public void setQuestion_description(String question_description) {
//        data.question_description = question_description;
//    }
//}
//
//@JsonIgnoreProperties(ignoreUnknown = true)
//class UserSecurityQuestionData {
//    int security_question_id;
//    String phone;
//    String question_description;
//
//    public int getSecurity_question_id() {
//        return security_question_id;
//    }
//
//    public void setSecurity_question_id(int security_question_id) {
//        this.security_question_id = security_question_id;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public String getQuestion_description() {
//        return question_description;
//    }
//
//    public void setQuestion_description(String question_description) {
//        this.question_description = question_description;
//    }
//}