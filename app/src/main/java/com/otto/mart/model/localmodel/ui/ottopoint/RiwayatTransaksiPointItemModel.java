package com.otto.mart.model.localmodel.ui.ottopoint;

public class RiwayatTransaksiPointItemModel {

    private String id;
    private String date;
    private String title;
    private String type_transaction;
    private long point;
    private String point_text;
    private boolean isPlus;

    public RiwayatTransaksiPointItemModel(String id, String date, String title, String type_transaction, long point, String point_text, boolean isPlus){
        this.id = id;
        this.date = date;
        this.title = title;
        this.type_transaction = type_transaction;
        this.point = point;
        this.point_text = point_text;
        this.isPlus = isPlus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType_transaction() {
        return type_transaction;
    }

    public void setType_transaction(String type_transaction) {
        this.type_transaction = type_transaction;
    }

    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
    }

    public String getPoint_text() {
        return point_text;
    }

    public void setPoint_text(String point_text) {
        this.point_text = point_text;
    }

    public boolean isPlus() {
        return isPlus;
    }

    public void setPlus(boolean plus) {
        isPlus = plus;
    }
}
