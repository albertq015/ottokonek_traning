package com.otto.mart.model.APIModel.Response.voucherGame;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.voucherGame.VoucherGameData;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VoucherGameResponse extends BaseResponseModel {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {

        private List<VoucherGameData> denomination;

        public List<VoucherGameData> getDenomination() {
            return denomination;
        }

        public void setDenomination(List<VoucherGameData> denomination) {
            this.denomination = denomination;
        }
    }
}
