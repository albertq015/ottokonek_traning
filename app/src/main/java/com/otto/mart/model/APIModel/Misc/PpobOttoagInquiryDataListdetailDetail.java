package com.otto.mart.model.APIModel.Misc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PpobOttoagInquiryDataListdetailDetail {
    private String period;
    private int amount;
    private int penalty;
    private String watermeter;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    public String getWatermeter() {
        return watermeter;
    }

    public void setWatermeter(String watermeter) {
        this.watermeter = watermeter;
    }
}