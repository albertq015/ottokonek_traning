package com.otto.mart.model.localmodel.State;


import com.otto.mart.model.APIModel.Misc.BusinessCategoryModel;

public class RegisterStateModel {

    private String name, merchant_name, email, phone, mother_name, dob, complete_address, referal_number, bank_code, account_number, account_name, pin, pin_confirmation, answer;
    private int posProv = -1, posKab = -1, posKec = -1, posKel = -1, posSecQ = -1, posBank = -1, province_id, city_id, district_id, village_id;
    private BusinessCategoryModel bcmState;

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

    public String getMother_name() {
        return mother_name;
    }

    public void setMother_name(String mother_name) {
        this.mother_name = mother_name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getComplete_address() {
        return complete_address;
    }

    public void setComplete_address(String complete_address) {
        this.complete_address = complete_address;
    }

    public String getReferal_number() {
        return referal_number;
    }

    public void setReferal_number(String referal_number) {
        this.referal_number = referal_number;
    }

    public String getBank_code() {
        return bank_code;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPin_confirmation() {
        return pin_confirmation;
    }

    public void setPin_confirmation(String pin_confirmation) {
        this.pin_confirmation = pin_confirmation;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getPosProv() {
        return posProv;
    }

    public void setPosProv(int posProv) {
        this.posProv = posProv;
    }

    public int getPosKab() {
        return posKab;
    }

    public void setPosKab(int posKab) {
        this.posKab = posKab;
    }

    public int getPosKec() {
        return posKec;
    }

    public void setPosKec(int posKec) {
        this.posKec = posKec;
    }

    public int getPosKel() {
        return posKel;
    }

    public void setPosKel(int posKel) {
        this.posKel = posKel;
    }

    public int getPosSecQ() {
        return posSecQ;
    }

    public void setPosSecQ(int posSecQ) {
        this.posSecQ = posSecQ;
    }

    public int getPosBank() {
        return posBank;
    }

    public void setPosBank(int posBank) {
        this.posBank = posBank;
    }

    public int getProvince_id() {
        return province_id;
    }

    public void setProvince_id(int province_id) {
        this.province_id = province_id;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public int getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(int district_id) {
        this.district_id = district_id;
    }

    public int getVillage_id() {
        return village_id;
    }

    public void setVillage_id(int village_id) {
        this.village_id = village_id;
    }

    public BusinessCategoryModel getBcmState() {
        return bcmState;
    }

    public void setBcmState(BusinessCategoryModel bcmState) {
        this.bcmState = bcmState;
    }
}
