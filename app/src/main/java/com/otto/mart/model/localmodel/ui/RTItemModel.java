package com.otto.mart.model.localmodel.ui;

import java.util.List;

public class RTItemModel {

    private String date;
    private int icon;
    private String id;
    private String name;
    private String jumlah;
    private String biyaya;
    private String komisi;
    private String title;
    private Boolean isInput = false, hasID = false, hasByy = false, hasKms = false;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getId() {
        hasID = true;
        return id;
    }

    public void setId(String id) {
        hasID = true;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getBiyaya() {
        return biyaya;
    }

    public void setBiyaya(String biyaya) {
        hasByy = true;
        this.biyaya = biyaya;
    }

    public String getKomisi() {
        return komisi;
    }

    public void setKomisi(String komisi) {
        hasKms = true;
        this.komisi = komisi;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getInput() {
        return isInput;
    }

    public void setInput(Boolean profit) {
        isInput = profit;
    }

    public Boolean getHasID() {
        return hasID;
    }

    public Boolean getHasByy() {
        return hasByy;
    }

    public Boolean getHasKms() {
        return hasKms;
    }

    public static class FAQUModel {
        private String title;
        private String TopContent;
        private List<String> midContent;
        private String bottomContent;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTopContent() {
            return TopContent;
        }

        public void setTopContent(String topContent) {
            TopContent = topContent;
        }

        public List<String> getMidContent() {
            return midContent;
        }

        public void setMidContent(List<String> midContent) {
            this.midContent = midContent;
        }

        public String getBottomContent() {
            return bottomContent;
        }

        public void setBottomContent(String bottomContent) {
            this.bottomContent = bottomContent;
        }
    }
}
