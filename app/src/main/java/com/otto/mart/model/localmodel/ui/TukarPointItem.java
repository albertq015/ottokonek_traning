package com.otto.mart.model.localmodel.ui;

public class TukarPointItem {

    private int id = -1;
    private long saldo_ottopay = 0;
    private long harga = 0;
    private boolean active = false;

    public TukarPointItem(int id, long saldo_ottopay, long harga){
        this.id = id;
        this.saldo_ottopay = saldo_ottopay;
        this.harga = harga;
    }

    public TukarPointItem(int id, long saldo_ottopay, boolean active){
        this.id = id;
        this.saldo_ottopay = saldo_ottopay;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getSaldo_ottopay() {
        return saldo_ottopay;
    }

    public void setSaldo_ottopay(long saldo_ottopay) {
        this.saldo_ottopay = saldo_ottopay;
    }

    public long getHarga() {
        return harga;
    }

    public void setHarga(long harga) {
        this.harga = harga;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
