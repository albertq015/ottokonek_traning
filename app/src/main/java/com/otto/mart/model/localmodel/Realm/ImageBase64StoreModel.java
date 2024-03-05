package com.otto.mart.model.localmodel.Realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ImageBase64StoreModel extends RealmObject {

    @PrimaryKey
    Integer SessionKey;
    String ktp;
    String ktporang;
    String etalase;

    public Integer getSessionKey() {
        return SessionKey;
    }

    public void setSessionKey(Integer sessionKey) {
        SessionKey = sessionKey;
    }

    public String getKtp() {
        return ktp;
    }

    public void setKtp(String ktp) {
        this.ktp = ktp;
    }

    public String getKtporang() {
        return ktporang;
    }

    public void setKtporang(String ktporang) {
        this.ktporang = ktporang;
    }

    public String getEtalase() {
        return etalase;
    }

    public void setEtalase(String etalase) {
        this.etalase = etalase;
    }
}
