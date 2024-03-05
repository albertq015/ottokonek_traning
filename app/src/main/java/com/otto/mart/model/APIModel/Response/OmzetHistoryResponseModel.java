package com.otto.mart.model.APIModel.Response;

import androidx.annotation.Keep;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.otto.mart.model.APIModel.Misc.history.OmzetHistory;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OmzetHistoryResponseModel extends BaseResponseModel {

    @Keep
    public OmzetHistoryResponseModel() {
    }

    @JsonProperty("data")
    private OmzetHistoryDataModel data;

    public OmzetHistoryDataModel getData() {
        return data;
    }

    public void setData(OmzetHistoryDataModel data) {
        this.data = data;
    }

    public List<OmzetHistory> getTransactions() {
        return data.getTransactions();
    }

    public void setTransactions(List<OmzetHistory> transactions) {
        this.data.setTransactions(transactions);
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class OmzetHistoryDataModel {

    @Keep
    public OmzetHistoryDataModel() {

    }

    @JsonProperty("transactions")
    public List<OmzetHistory> transactions;

    public List<OmzetHistory> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<OmzetHistory> transactions) {
        this.transactions = transactions;
    }
}


