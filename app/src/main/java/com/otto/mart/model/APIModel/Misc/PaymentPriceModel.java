package com.otto.mart.model.APIModel.Misc;

public class PaymentPriceModel {
    private long modal;
    private long komisi;
    private long total;
    private String thumb;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getModal() {
        return modal;
    }

    public void setModal(long modal) {
        this.modal = modal;
    }

    public long getKomisi() {
        return komisi;
    }

    public void setKomisi(long komisi) {
        this.komisi = komisi;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
