package com.otto.mart.model.APIModel.Misc.tokoOttopay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Store {
    private int id;
    private String name;
    private Logo logo;
    private int min_order;
    private String down_payment;
    private List<String> tech_requirement;
    private List<String> doc_requirement;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Logo getLogo() {
        return logo;
    }

    public void setLogo(Logo logo) {
        this.logo = logo;
    }

    public int getMin_order() {
        return min_order;
    }

    public void setMin_order(int min_order) {
        this.min_order = min_order;
    }

    public String getDown_payment() {
        return down_payment;
    }

    public void setDown_payment(String down_payment) {
        this.down_payment = down_payment;
    }

    public List<String> getTech_requirement() {
        return tech_requirement;
    }

    public void setTech_requirement(List<String> tech_requirement) {
        this.tech_requirement = tech_requirement;
    }

    public List<String> getDoc_requirement() {
        return doc_requirement;
    }

    public void setDoc_requirement(List<String> doc_requirement) {
        this.doc_requirement = doc_requirement;
    }

    public static class Logo {
        private String url;
        private Small small;
        private Thumb thumb;
        private Big big;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Small getSmall() {
            return small;
        }

        public void setSmall(Small small) {
            this.small = small;
        }

        public Thumb getThumb() {
            return thumb;
        }

        public void setThumb(Thumb thumb) {
            this.thumb = thumb;
        }

        public Big getBig() {
            return big;
        }

        public void setBig(Big big) {
            this.big = big;
        }

        public static class Small {
            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class Thumb {

            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class Big {

            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
