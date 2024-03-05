package com.otto.mart.model.APIModel.Request;

public class GetQrStringRequestModel {
    private String pan;
    private int trx_amount;
    private String bill_id;
    private String terminal_id;

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public int getTrx_amount() {
        return trx_amount;
    }

    public void setTrx_amount(int trx_amount) {
        this.trx_amount = trx_amount;
    }

    public String getBill_id() {
        return bill_id;
    }

    public void setBill_id(String bill_id) {
        this.bill_id = bill_id;
    }

    public String getTerminal_id() {
        return terminal_id;
    }

    public void setTerminal_id(String terminal_id) {
        this.terminal_id = terminal_id;
    }
}
