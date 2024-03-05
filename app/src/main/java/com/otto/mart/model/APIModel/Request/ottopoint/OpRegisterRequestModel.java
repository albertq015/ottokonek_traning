package com.otto.mart.model.APIModel.Request.ottopoint;

public class OpRegisterRequestModel {

    private String firstName;
    private String lastname;
    private String email;
    private String phone;
    private String agreement1;
    private String institution = "Ottopay";

    public OpRegisterRequestModel(String firstName, String lastname, String email, String phone, String agreement1){
        this.firstName = firstName;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.agreement1 = agreement1;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAgreement1() {
        return agreement1;
    }

    public void setAgreement1(String agreement1) {
        this.agreement1 = agreement1;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }
}
