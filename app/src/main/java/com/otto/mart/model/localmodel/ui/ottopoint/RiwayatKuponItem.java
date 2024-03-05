package com.otto.mart.model.localmodel.ui.ottopoint;

public class RiwayatKuponItem {

    private int id;
    private String date;
    private String companyCode;
    private String status;
    private long harga = 0;
    private boolean isPlus = false;

    public RiwayatKuponItem(int id, String date, String companyCode, long harga, boolean isPlus){
        this.id = id;
        this.date = date;
        this.companyCode = companyCode;
        this.harga = harga;
        this.isPlus = isPlus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public long getHarga() {
        return harga;
    }

    public void setHarga(long harga) {
        this.harga = harga;
    }

    public boolean isPlus() {
        return isPlus;
    }

    public void setPlus(boolean plus) {
        isPlus = plus;
    }
}
