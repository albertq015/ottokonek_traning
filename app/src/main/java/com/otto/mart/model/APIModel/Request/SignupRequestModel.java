package com.otto.mart.model.APIModel.Request;

import com.otto.mart.model.APIModel.Misc.bank.BankRequestModel;

import java.util.List;

public class SignupRequestModel {
    String name;
    String merchant_name;
    String email;
    String phone;
    String mother_name;
    String merchant_id;
    long dob;
    String complete_address;
    int province_id = -1;
    int city_id = -1;
    int district_id = -1;
    long village_id = -1;
    String referal_number;
    boolean rose;
    List<BankRequestModel> banks;
    String pin;
    String pin_confirmation;
    int question_id;
    String answer;
    String firebase_token;
    int business_category_id;
    double latitude;
    double longitude;
    String id_card_type;
    String id_card_photo;
    String store_photo;
    String bin;
    String account_number;

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
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

    public long getDob() {
        return dob;
    }

    public void setDob(long dob) {
        this.dob = dob;
    }

    public String getComplete_address() {
        return complete_address;
    }

    public void setComplete_address(String complete_address) {
        this.complete_address = complete_address;
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

    public long getVillage_id() {
        return village_id;
    }

    public void setVillage_id(long village_id) {
        this.village_id = village_id;
    }

    public String getReferal_number() {
        return referal_number;
    }

    public void setReferal_number(String referal_number) {
        this.referal_number = referal_number;
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

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public String getFirebase_token() {
        return firebase_token;
    }

    public void setFirebase_token(String firebase_token) {
        this.firebase_token = firebase_token;
    }

    public List<BankRequestModel> getBanks() {
        return banks;
    }

    public void setBanks(List<BankRequestModel> banks) {
        this.banks = banks;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


    public int getBusiness_category_id() {
        return business_category_id;
    }

    public void setBusiness_category_id(int business_category_id) {
        this.business_category_id = business_category_id;
    }

    public boolean isRose() {
        return rose;
    }

    public void setRose(boolean rose) {
        this.rose = rose;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getId_card_type() {
        return id_card_type;
    }

    public void setId_card_type(String id_card_type) {
        this.id_card_type = id_card_type;
    }

    public String getId_card_photo() {
        return id_card_photo;
    }

    public void setId_card_photo(String id_card_photo) {
        this.id_card_photo = id_card_photo;
    }

    public String getStore_photo() {
        return store_photo;
    }

    public void setStore_photo(String store_photo) {
        this.store_photo = store_photo;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }
}


